package com.ming.m_blog.strategy.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ming.m_blog.constant.CommonConst;
import com.ming.m_blog.dto.search.ArticleSearchDTO;
import com.ming.m_blog.enums.ArticleStatusEnum;
import com.ming.m_blog.vo.SearchVO;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 使用es搜索文章
 */
@Log4j2
@Service("esArticleSearchImpl")
public class EsArticleSearchImpl extends AbstractSearchStrategy{

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public SearchVO search(String keyword) {
        // 判空
        if (StringUtils.isEmpty(keyword)){
            return SearchVO.builder().build();
        }

        return SearchVO.builder()
                .articleList(doSearch(buildQuery(keyword)))
                .build();
    }

    /**
     * 构造条件构造器
     * @param keyword 查询关键词
     * @return        条件构造器
     */
    private NativeSearchQueryBuilder buildQuery(String keyword){
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // // 按照标题检索
        // boolQueryBuilder.should(QueryBuilders.matchQuery("title",keyword));
        // // 按照文章检索
        // boolQueryBuilder.should(QueryBuilders.matchQuery("content",keyword));
        // // 状态规定
        // boolQueryBuilder.must(QueryBuilders.termQuery("isDelete", CommonConst.FALSE));
        // boolQueryBuilder.must(QueryBuilders.termQuery("status", ArticleStatusEnum.PUBLIC.getStatus()));

        boolQueryBuilder
                .must(QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchQuery("title", keyword))
                        .should(QueryBuilders.matchQuery("content", keyword))
                )
                .must(QueryBuilders.termQuery("isDelete", CommonConst.FALSE))
                .must(QueryBuilders.termQuery("status", ArticleStatusEnum.PUBLIC.getStatus()));
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        return nativeSearchQueryBuilder;
    }

    /**
     * 执行搜索
     * @param nativeSearchQueryBuilder 条件构造器
     * @return                         搜索结果
     */
    private List<ArticleSearchDTO> doSearch(NativeSearchQueryBuilder nativeSearchQueryBuilder){
        // 文章标题高亮
        HighlightBuilder.Field titleField = new HighlightBuilder.Field("title");
        titleField.preTags(CommonConst.PRE_TAG);
        titleField.postTags(CommonConst.POST_TAG);
        // 文章内容高亮
        HighlightBuilder.Field contentField = new HighlightBuilder.Field("content");
        contentField.preTags(CommonConst.PRE_TAG);
        contentField.postTags(CommonConst.POST_TAG);
        // 高亮绑定条件构造器
        nativeSearchQueryBuilder.withHighlightFields(titleField, contentField);
        // 进行搜索
        try {
            SearchHits<ArticleSearchDTO> searchHit = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), ArticleSearchDTO.class);
            return searchHit.getSearchHits().stream().map(hit -> {
                ArticleSearchDTO articleSearchDTO = hit.getContent();
                // 获取高亮数据，修改到查询结果上
                List<String> titleHighLightList = hit.getHighlightFields().get("title");
                if (CollectionUtils.isNotEmpty(titleHighLightList)){
                    articleSearchDTO.setTitle(titleHighLightList.get(0));
                }
                // 获取搜索信息的高亮数据
                List<String> contentHighLightList = hit.getHighlightFields().get("content");
                if (CollectionUtils.isNotEmpty(contentHighLightList)){
                    articleSearchDTO.setContent(contentHighLightList.get(0));
                }
                return articleSearchDTO;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();
    }

}
