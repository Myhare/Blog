package com.ming.m_blog.utils;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Objects;

/**
 * 分页工具类
 */
public class PageUtils {

    public static final ThreadLocal<Page<?>> PAGE_HOLDER = new ThreadLocal<>();

    // 设置分页页码
    public static void setPageNum(Page<?> page){
        PAGE_HOLDER.set(page);
    }

    // 获取分页信息
    public static Page<?> getPage(){
        Page<?> page = PAGE_HOLDER.get();
        /*
             如果当前线程没有page对象，新建一个对象，并且new一个Page存进去，new一个Page会有MybatisPlus的默认值
             new出来的默认值current为1，size为10
         */
        if (Objects.isNull(page)){
            setPageNum(new Page<>());
        }
        return PAGE_HOLDER.get();
    }

    public static Long getCurrent() {
        return getPage().getCurrent();
    }

    public static Long getSize() {
        return getPage().getSize();
    }

    public static Long getLimitCurrent() {
        return (getCurrent() - 1) * getSize();
    }

    public static void remove() {
        PAGE_HOLDER.remove();
    }

}
