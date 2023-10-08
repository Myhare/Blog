package com.ming.m_blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件路径枚举
 *
 * @author ming
 * @date 2021/08/04
 */
@Getter
@AllArgsConstructor
public enum FilePathEnum {
    /**
     * 头像路径
     */
    AVATAR("avatar/", "头像路径"),
    /**
     * 文章图片路径
     */
    ARTICLE("articles/", "文章图片路径"),
    /**
     * 配置网站配置信息路径
     */
    ICON("configImage/","网站配置图片路径"),

    /**
     * 说说图片路径
     */
    TALK("talk/", "说说图片路径");

    /**
     * 路径
     */
    private final String path;

    /**
     * 描述
     */
    private final String desc;

}
