package com.ming.m_blog.config;

import com.ming.m_blog.hander.AuthenticationFailHandlerImpl;
import com.ming.m_blog.hander.AuthenticationSuccessHandlerImpl;
import com.ming.m_blog.hander.LogoutTokenHandlerImpl;
import com.ming.m_blog.hander.LogoutSuccessHandlerImpl;
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

    // 防止用户重复登录
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
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler)   // 登录成功处理
                .failureHandler(authenticationFailHandler)      // 登录失败处理
                .and()
                .logout()
                .logoutUrl("/logout")
                .addLogoutHandler(logoutHandler)        // 登出处理
                .logoutSuccessHandler(logoutSuccessHandler);    // 登出成功处理

        // session管理
        http.sessionManagement()
                .maximumSessions(1) // 最大session数
                .maxSessionsPreventsLogin(true)  // 某用户达到最大会话并发数后，新会话请求会被拒绝登录
                .sessionRegistry(sessionRegistry());

        // 关闭csrf跨站防护
        http.csrf().disable()
                .cors();

        // 路由权限配置
        http.authorizeRequests()
                // 放行所有接口
                .anyRequest().permitAll();

    }

}
