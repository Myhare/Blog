package com.ming.m_blog.mapper.elasticSearch;

import com.ming.m_blog.dto.search.ArticleSearchDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 文章es操作
 */
public interface ArticleEsMapper extends ElasticsearchRepository<ArticleSearchDTO, Integer> {



}
