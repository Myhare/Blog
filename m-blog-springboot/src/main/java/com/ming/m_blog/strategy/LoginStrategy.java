package com.ming.m_blog.strategy;

import com.ming.m_blog.dto.user.UserInfoDTO;

/**
 * 第三方策略模式
 */
public interface LoginStrategy {

    public UserInfoDTO login(String data);

}
