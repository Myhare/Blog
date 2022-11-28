package com.ming.m_blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.ming.m_blog.mapper")
// @EnableAsync  // 启用异步任务
@EnableScheduling       // 开启定时任务
@SpringBootApplication
public class MBlogSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MBlogSpringbootApplication.class, args);
    }

}
