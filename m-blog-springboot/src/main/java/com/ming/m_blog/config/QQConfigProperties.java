package com.ming.m_blog.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "qq")
public class QQConfigProperties {

    /**
     * 应用APPId
     */
    private String appId;

    /**
     * 检查token的api
     */
    private String checkTokenUrl;

    /**
     * 获取登录用户信息的api
     */
    private String userInfoUrl;
}
