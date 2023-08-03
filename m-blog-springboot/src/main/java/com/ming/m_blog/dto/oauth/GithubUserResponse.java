package com.ming.m_blog.dto.oauth;

import lombok.Data;

/**
 * GitHub获取用户信息
 */
@Data
public class GithubUserResponse {

    /**
     * 唯一id
     */
    private String id;

    /**
     * 登录用户名
     */
    private String login;

    /**
     * 头像路径
     */
    private String avatar_url;


}
