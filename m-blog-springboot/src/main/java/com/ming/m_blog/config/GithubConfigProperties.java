package com.ming.m_blog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "github")
public class GithubConfigProperties {

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 客户端密钥
     */
    private String clientSecret;

    /**
     * 获取token的url
     */
    private String getTokenUrl;

    /**
     * 获取用户信息的url
     */
    private String getUserUrl;
}
