package com.ming.m_blog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {

    /**
     * 秒数
     */
    int seconds();

    /**
     * 指定秒数最大的访问次数
     */
    int maxCount();

    /**
     * 提示信息
     */
    String message() default "请求过于频繁";
}
