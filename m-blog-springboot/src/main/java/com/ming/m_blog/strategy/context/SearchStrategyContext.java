package com.ming.m_blog.strategy.context;


import com.ming.m_blog.enums.SearchModelEnum;
import com.ming.m_blog.enums.SearchTypeEnum;
import com.ming.m_blog.strategy.SearchStrategy;
import com.ming.m_blog.vo.SearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 搜索上下文
 */
@Service
public class SearchStrategyContext {

    @Value("${search.mode}")
    private String searchMode;

    @Autowired
    private Map<String, SearchStrategy> searchStrategyMap;

    /**
     * 执行搜索策略
     * @param keyword 搜索关键词
     * @return        搜索结果
     */
    public SearchVO executeSearchArticle(String keyword, Integer searchType){
        return searchStrategyMap.get(SearchModelEnum.getStrategy(searchMode)).search(keyword, SearchTypeEnum.getSearchTypeEnum(searchType));
    }

}
