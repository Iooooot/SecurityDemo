package com.wht.handler;

import com.alibaba.fastjson.JSON;
import com.wht.entity.APIException;
import com.wht.entity.ResponseEntityDemo;
import com.wht.utils.WebUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class SecurityAPIExceptionHandlerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest,servletResponse);
        }catch (APIException e){
            /**
             * 捕捉出现的异常，根据自己的需求进行处理
             * ResponseEntityDemo 自定义的响应结果封装类
             * APIException 自定义异常
             */
            servletResponse.setCharacterEncoding("UTF-8");
            WebUtils.renderString((HttpServletResponse) servletResponse, JSON.toJSONString(ResponseEntityDemo.failed(e.getCode(),e.getMessage())));
        }
    }
}
