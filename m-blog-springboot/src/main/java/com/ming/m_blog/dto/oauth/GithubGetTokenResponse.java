package com.ming.m_blog.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GitHub获取token信息响应参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GithubGetTokenResponse {

    /**
     * 获取到的token
     */
    private String access_token;

    /**
     * token类型
     */
    private String token_type;


    private String scope;

}
