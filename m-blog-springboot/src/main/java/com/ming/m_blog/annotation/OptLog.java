package com.ming.m_blog.annotation;

import java.lang.annotation.*;

/**
 * AOP注解实现日志
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OptLog {

    /**
     * 操作类型
     */
    String optType() default "";

}
