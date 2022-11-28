package com.ming.m_blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Bean(value = "createRestApi")
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //分组名称
                .groupName("个人博客API")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.ming.m_blog.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    // 配置ApiInfo，替代原来的默认配置
    public ApiInfo apiInfo(){
        // 作者信息
        Contact contact = new Contact("Ming", "https://home.cnblogs.com/u/lzmBlogs", "1940307627@qq.com"); // 第二个参数写个人的站点

        return new ApiInfo(
                "个人博客api文档",
                "springboot+vue的个人开发项目",
                "1.0",
                "https://home.cnblogs.com/u/lzmBlogs", // termsOfServiceUrl 服务条款网址  这里写你组织的url
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<VendorExtension>()
        );
    }

}
