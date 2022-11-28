package com.ming.m_blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 博客状态
 */
@Getter
@AllArgsConstructor
public enum ArticleStatusEnum {

    DRAFT(0, "草稿"),

    PUBLIC(1,"公开"),

    SECRET(2,"私密");


    private final Integer status;

    private final String desc;

    // 通过status获取Enum
    public static ArticleStatusEnum getArticleStatus(Integer status){
        for (ArticleStatusEnum articleStatusEnum : ArticleStatusEnum.values()) {
            if (articleStatusEnum.status.equals(status)){
                return articleStatusEnum;
            }
        }
        return null;
    }

}
