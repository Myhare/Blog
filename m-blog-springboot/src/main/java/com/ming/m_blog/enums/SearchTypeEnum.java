package com.ming.m_blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 搜索类型枚举
 */
@Getter
@AllArgsConstructor
public enum SearchTypeEnum {

    ARTICLE(1, "文章","mysqlArticleSearchImpl"),

    CATEGORY(2,"分类","mysqlCategorySearchImpl"),

    TAG(3,"标签","mysqlTagSearchImpl"),

    PICTURE(4,"图片","searchPictureStrategyImpl");


    private final Integer type;

    private final String desc;

    /**
     * 搜索此类型数据使用的方法
     */
    private final String strategy;

    // 通过status获取Enum
    public static SearchTypeEnum getSearchTypeEnum(Integer type){
        for (SearchTypeEnum articleStatusEnum : SearchTypeEnum.values()) {
            if (articleStatusEnum.type.equals(type)){
                return articleStatusEnum;
            }
        }
        return null;
    }

}
