package com.ming.m_blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 说说状态枚举
 */
@Getter
@AllArgsConstructor
public enum TalkStatusEnum {

    /**
     * 公开
     */
    PUBLIC(1, "公开"),
    /**
     * 私密
     */
    SECRET(2, "私密");

    /**
     * 状态
     */
    private final Integer status;

    /**
     * 描述
     */
    private final String desc;

}
