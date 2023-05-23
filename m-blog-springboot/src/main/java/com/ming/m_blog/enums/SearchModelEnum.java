package com.ming.m_blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 搜索模式枚举
 */
@Getter
@AllArgsConstructor
public enum SearchModelEnum {

    /**
     * 本地MySQL搜索
     */
    mysql("mysql","mysqlSearchStrategyImpl"),

    /**
     * elasticSearch模式搜索
     */
    elasticSearch("elasticSearch","esSearchStrategyImpl");

    private final String mode;

    private final String strategy;

    /**
     * 获取搜索策略
     * @param mode 当前使用的搜索模式
     * @return     使用的具体策略
     */
    public static String getStrategy(String mode){
        for (SearchModelEnum value : SearchModelEnum.values()) {
            if (value.getMode().equals(mode)){
                return value.getStrategy();
            }
        }
        return null;
    }

}
