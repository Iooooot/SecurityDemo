package com.wht.Filter;


import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.wht.entity.APIException;
import com.wht.entity.LoginUser;
import com.wht.utils.CusAccessObjectUtil;
import com.wht.utils.JWTUtils;
import com.wht.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }

        //解析token
        try {
            JWTUtils.verifyToken(token, CusAccessObjectUtil.getIpAddress(request));
        } catch (SignatureVerificationException e) {
            throw new APIException(401,"签名失效,请重新登录");
        } catch (TokenExpiredException e) {
            redisUtil.del("login:token:"+JWTUtils.getClaimByName(token,"uid").asInt());
            throw new APIException(401,"token过期,请重新登录");
        } catch (AlgorithmMismatchException e) {
            throw new APIException(401,"token算法不一致,请重新登录");
        } catch (Exception e) {
            throw new APIException(401,"token无效,请重新登录");

        }

        //判断token是否跟redis中一致，不一致说明已经被其他客户端挤掉
        String cacheToken = (String) redisUtil.get("login:token:" + JWTUtils.getClaimByName(token, "uid").asInt());
        if(cacheToken != null && !cacheToken.equals(token)){
            throw new APIException(401,"您已在另一台设备登录，本次登录已下线!");
        }
        //从redis中获取用户信息
        String redisKey = "login:" + JWTUtils.getClaimByName(token,"uid").asInt();
        LoginUser loginUser = (LoginUser) redisUtil.get(redisKey);
        if(Objects.isNull(loginUser)){
            throw new APIException(401,"用户未登录");
        }
        //存入SecurityContextHolder
        //TODO 获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}
