package com.ming.m_blog.mapper.elasticSearch;

import com.ming.m_blog.dto.search.TagSearchDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 标签es操作
 */
public interface TagEsMapper extends ElasticsearchRepository<TagSearchDTO, Integer> {
}
