package com.ming.m_blog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    USER(1,"用户"),

    VISITOR(2,"游客");

    private final Integer type;

    private final String desc;

    // 获取enum
    public static UserTypeEnum getUserTypeEnum(Integer type){
        for (UserTypeEnum userTypeEnum : UserTypeEnum.values()) {
            if (userTypeEnum.getType().equals(type)){
                return userTypeEnum;
            }
        }
        return null;
    }

}
