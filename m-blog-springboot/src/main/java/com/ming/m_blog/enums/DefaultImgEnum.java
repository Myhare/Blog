package com.ming.m_blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 默认图片枚举
 */
@Getter
@AllArgsConstructor
public enum DefaultImgEnum {

    /**
     * 默认文章封面
     */
    DEFAULT_ARTICLE_COVER("http://www.static.mingzib.xyz/blogGetDefaultFile/deafaultArticleCover.png","默认文章封面"),

    /**
     * 默认网站头像
     */
    DEFAULT_WEBSITE_AVATAR("http://www.static.mingzib.xyz/blogGetDefaultFile/defaultMyAvatar.jpg","默认网站头像"),

    /**
     * 游客头像
     */
    DEFAULT_TOURIST_AVATAR("http://www.mingzib.xyz/blogGetDefaultFile/touristAvatar.jpg","游客头像");

    private final String url;

    private final String desc;
}
