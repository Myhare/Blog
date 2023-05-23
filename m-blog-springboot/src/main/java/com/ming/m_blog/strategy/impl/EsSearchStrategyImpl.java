package com.ming.m_blog.strategy.impl;

import com.ming.m_blog.enums.SearchTypeEnum;
import com.ming.m_blog.strategy.SearchStrategy;
import com.ming.m_blog.vo.SearchVO;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * ES搜索策略
 */
@Service("esSearchStrategyImpl")
public class EsSearchStrategyImpl implements SearchStrategy {

    @Resource
    private EsArticleSearchImpl esArticleSearch;
    @Resource
    private EsCategorySearchImpl esCategorySearch;
    @Resource
    private EsTagSearchImpl esTagSearch;
    @Resource
    SearchPictureStrategyImpl searchPictureStrategy;

    private Map<Integer, AbstractSearchStrategy> searchStrategyMap;

    // 初始化项目的时候调用下面的方法,初始化搜索策略
    @PostConstruct
    public void init(){
        // System.out.println("EsSearchStrategyImpl--->成功进入init方法");
        searchStrategyMap = new HashMap(){{
            put(SearchTypeEnum.ARTICLE.getType(),esArticleSearch);
            put(SearchTypeEnum.TAG.getType(), esTagSearch);
            put(SearchTypeEnum.CATEGORY.getType(),esCategorySearch);
            put(SearchTypeEnum.PICTURE.getType(),searchPictureStrategy);
        }};
    }

    @Override
    public SearchVO search(String keyword, SearchTypeEnum searchTypeEnum) {
        return searchStrategyMap.get(searchTypeEnum.getType()).search(keyword);
    }
}
