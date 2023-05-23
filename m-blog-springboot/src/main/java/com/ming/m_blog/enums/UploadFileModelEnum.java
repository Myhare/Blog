package com.ming.m_blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 上传模式枚举
 */
@Getter
@AllArgsConstructor
public enum UploadFileModelEnum {

    /**
     * 本地上传
     */
    LOCAL("local","localUploadStrategyImpl"),

    QINIU("qiniu","qiNiuUploadStrategyImpl");

    private final String mode;

    private final String strategy;


    /**
     * 获取策略
     * @param mode 上传模式
     * @return     选择的策略模式
     */
    public static String getStrategy(String mode){
        for (UploadFileModelEnum value : UploadFileModelEnum.values()) {
            if (value.getMode().equals(mode)){
                return value.getStrategy();
            }
        }
        return null;
    }
}
