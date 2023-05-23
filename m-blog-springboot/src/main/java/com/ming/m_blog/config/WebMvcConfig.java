package com.ming.m_blog.config;

import com.ming.m_blog.interceptor.AccessLimitInterceptor;
import com.ming.m_blog.interceptor.PageableHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    // 接口限流过滤器
    @Autowired
    private AccessLimitInterceptor accessLimitInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路由
        registry.addMapping("/**")//项目中的所有接口都支持跨域
                .allowedOrigins("*")//所有地址都可以访问，也可以配置具体地址
                .allowCredentials(true) //是否允许请求带有验证信息
                .allowedMethods("*")//"GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"
                .allowedHeaders("*").maxAge(3600);// 跨域允许时间
    }

    // 处理静态资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    // 添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessLimitInterceptor);      // 添加接口限流拦截器
        registry.addInterceptor(new PageableHandlerInterceptor());  // 添加分页拦截器
    }
}
