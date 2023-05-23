package com.ming.m_blog.strategy;

import com.ming.m_blog.enums.SearchTypeEnum;
import com.ming.m_blog.vo.SearchVO;

/**
 * 搜索策略模式
 */
public interface SearchStrategy {

    /**
     * 搜索文章
     * @param keyword 搜索关键词
     * @param searchTypeEnum 搜索类型枚举
     * @return        查询到的文章结果
     */
    public SearchVO search(String keyword, SearchTypeEnum searchTypeEnum);

}
