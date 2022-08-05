package com.wht.config;

import com.alibaba.fastjson.JSON;
import com.wht.Filter.JwtAuthenticationTokenFilter;
import com.wht.entity.ResponseEntityDemo;
import com.wht.entity.ResultCode;
import com.wht.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @作者 江南一点雨
 * @公众号 江南一点雨
 * @微信号 a_java_boy
 * @GitHub https://github.com/lenve
 * @博客 http://wangsong.blog.csdn.net
 * @网站 http://www.javaboy.org
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;


    // @Override
    // @Bean
    // protected UserDetailsService userDetailsService() {
    //     InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    //     manager.createUser(User.withUsername("admin").password("123456").roles("admin").build());
    //     manager.createUser(User.withUsername("user").password("123456").roles("user").build());
    //     return manager;
    // }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**","/images/**","/verifyCode");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // @Bean
    // LoginFilter loginFilter() throws Exception {
    //     LoginFilter loginFilter = new LoginFilter();
    //     loginFilter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
    //         @Override
    //         public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    //             response.setContentType("application/json;charset=utf-8");
    //             PrintWriter out = response.getWriter();
    //             // MyUserDetail user = (MyUserDetail) ;
    //             String s = new ObjectMapper().writeValueAsString(ResponseEntityDemo.successWithData(authentication.getPrincipal()));
    //             out.write(s);
    //             out.flush();
    //             out.close();
    //         }
    //     });
    //     loginFilter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
    //         @Override
    //         public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    //             response.setContentType("application/json;charset=utf-8");
    //             PrintWriter out = response.getWriter();
    //             ResponseEntityDemo responseEntityDemo = ResponseEntityDemo.failed(exception.getMessage());
    //             if (exception instanceof LockedException) {
    //                 responseEntityDemo.setMessage("账户被锁定，请联系管理员!");
    //             } else if (exception instanceof CredentialsExpiredException) {
    //                 responseEntityDemo.setMessage("密码过期，请联系管理员!");
    //             } else if (exception instanceof AccountExpiredException) {
    //                 responseEntityDemo.setMessage("账户过期，请联系管理员!");
    //             } else if (exception instanceof DisabledException) {
    //                 responseEntityDemo.setMessage("账户被禁用，请联系管理员!");
    //             } else if (exception instanceof BadCredentialsException) {
    //                 responseEntityDemo.setMessage("用户名或者密码输入错误，请重新输入!");
    //             }
    //             out.write(new ObjectMapper().writeValueAsString(responseEntityDemo));
    //             out.flush();
    //             out.close();
    //         }
    //     });
    //     loginFilter.setAuthenticationManager(authenticationManagerBean());
    //     loginFilter.setFilterProcessesUrl("/doLogin");
    //     return loginFilter;
    // }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers("/user/login").anonymous()
                .anyRequest().authenticated()
                .and()
                //不通过Session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable();

        http.cors();
        //在UsernamePasswordAuthenticationFilter过滤器之前拦截
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //配置异常处理器
        http.exceptionHandling()
            .authenticationEntryPoint((req, resp, authException) -> {
                WebUtils.renderString(resp,JSON.toJSONString(ResponseEntityDemo.failed(ResultCode.UNAUTHORIZED)));
            })
            .accessDeniedHandler((req, resp, authException) -> {
                WebUtils.renderString(resp,JSON.toJSONString(ResponseEntityDemo.failed(ResultCode.FORBIDDEN)));
            });
    }
}
