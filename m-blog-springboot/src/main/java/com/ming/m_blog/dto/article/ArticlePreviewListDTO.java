package com.ming.m_blog.dto.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 文章预览列表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticlePreviewListDTO {

    /**
     * 文章列表
     */
    private List<ArticlePreviewDTO> articlePreviewDTOList;

    /**
     * 条件名
     */
    private String name;

}
