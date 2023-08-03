package com.ming.m_blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录方式枚举
 *
 * @date 2023-4-18
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {
    /**
     * 邮箱登录
     */
    EMAIL(1, "邮箱或用户名登录", ""),
    /**
     * QQ登录
     */
    QQ(2, "QQ登录", "qqLoginStrategyImpl"),

    /**
     * GitHub登录
     */
    GIT_HUB(3, "GitHub登录", "githubLoginStrategyImpl");
    // /**
    //  * 微博登录
    //  */
    // WEIBO(3, "微博登录", "weiboLoginStrategyImpl");

    /**
     * 登录方式
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 策略
     */
    private final String strategy;


}
