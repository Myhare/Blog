package com.ming.m_blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 评论类型枚举
 */
@Getter
@AllArgsConstructor
public enum CommentTypeEnum {

    /**
     * 文章
     */
    ARTICLE(1, "文章", "articles/"),
    /**
     * 友链
     */
    FRIEND_LINK(2, "友链", "links/"),

    /**
     * 说说
     */
    TALK(3, "说说", "talks/");

    /**
     * 评论类型
     */
    private final Integer type;


    /**
     * 策略
     */
    private final String desc;

    /**
     * 评论路径
     */
    private final String path;

    public static CommentTypeEnum getCommentTypeEnum(Integer type){
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()) {
            if (commentTypeEnum.type.equals(type)){
                return commentTypeEnum;
            }
        }
        return null;
    }


}
