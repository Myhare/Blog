package com.ming.m_blog.config;

import com.ming.m_blog.hander.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)  // 开启全局权限认证
// @EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // 登录成功处理器
    @Autowired
    private AuthenticationSuccessHandlerImpl authenticationSuccessHandler;
    // 登录失败处理器
    @Autowired
    private AuthenticationFailHandlerImpl authenticationFailHandler;
    // 登出成功处理器
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;
    // 登出处理类
    @Autowired
    private LogoutTokenHandlerImpl logoutHandler;
    // 权限不足
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    // 用户未登录处理
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;


    private final String LOGIN_URI = "/login";

    // 创建一个BCryptPasswordEncoding注入容器,设置密码验证方法
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); // 密码验证默认使用BCryptPassword加密
    }

    // 重写方法使得AuthenticationManager暴露出去,登录验证需要使用
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // 防止用户重复登录,监听session
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    // 注入SessionRegistry 获取在线用户信息
    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 配置登录注销路径
        http.formLogin()
                .loginProcessingUrl(LOGIN_URI)
                .successHandler(authenticationSuccessHandler)   // 登录成功处理
                .failureHandler(authenticationFailHandler)      // 登录失败处理
                .and()
                .logout()
                .logoutUrl("/logout")
                .addLogoutHandler(logoutHandler)        // 登出处理
                .logoutSuccessHandler(logoutSuccessHandler);    // 登出成功处理

        // session管理
        http.sessionManagement()
                .maximumSessions(10) // 最大session数
                .maxSessionsPreventsLogin(false)  // 某用户达到最大会话并发数后，新会话请求会被拒绝登录
                .expiredSessionStrategy(new SessionInformationExpiredStrategyImpl()) // 监听session消除后的处理
                .sessionRegistry(sessionRegistry());

        // 关闭csrf跨站防护
        http.csrf().disable()
                .exceptionHandling()
                // 权限不足处理
                .accessDeniedHandler(accessDeniedHandler)
                // 用户未登录处理
                .authenticationEntryPoint(authenticationEntryPoint);

        // 路由权限配置
        http.authorizeRequests()
                // 放行所有接口
                .anyRequest().permitAll();

    }

}
