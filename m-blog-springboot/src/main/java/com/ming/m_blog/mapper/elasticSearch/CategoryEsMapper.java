package com.ming.m_blog.mapper.elasticSearch;


import com.ming.m_blog.dto.search.CategorySearchDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 分类es操作
 */
public interface CategoryEsMapper extends ElasticsearchRepository<CategorySearchDTO, Integer> {
}
