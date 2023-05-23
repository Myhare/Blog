package com.ming.m_blog.strategy.impl;

import com.ming.m_blog.vo.SearchVO;

/**
 * 抽象搜索策略
 */
public abstract class AbstractSearchStrategy {

    /**
     * 搜索功能
     * @param keyword 搜索关键字
     * @return        搜索结果
     */
    public abstract SearchVO search(String keyword);

}
