package com.ming.m_blog.strategy.impl;

import com.ming.m_blog.enums.SearchTypeEnum;
import com.ming.m_blog.strategy.SearchStrategy;
import com.ming.m_blog.vo.SearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 本地MySQL搜索策略.注册器模式(使用map或者其他其他类型存储好后面要调用的对象)
 */
@Service("mysqlSearchStrategyImpl")
public class MysqlSearchStrategyImpl implements SearchStrategy {

    /**
     * 搜索类型选择
     */
    @Autowired
    private Map<String, AbstractSearchStrategy> searchStrategyMap;

    public SearchVO search(String keyword, SearchTypeEnum searchTypeEnum) {
        return searchStrategyMap.get(searchTypeEnum.getStrategy()).search(keyword);
    }
}
