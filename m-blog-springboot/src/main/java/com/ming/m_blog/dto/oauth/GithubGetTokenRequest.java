package com.ming.m_blog.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GitHub获取token信息请求参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GithubGetTokenRequest {

    /**
     * 客户端id
     */
    private String client_id;

    /**
     * 客户点密钥
     */
    private String client_secret;

    /**
     * 用户code
     */
    private String code;

}
