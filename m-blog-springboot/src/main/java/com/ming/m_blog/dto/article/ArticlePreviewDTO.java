package com.ming.m_blog.dto.article;

import com.ming.m_blog.dto.tag.TagSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 预览文章信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticlePreviewDTO {

    /**
     * 文章id
     */
    private Integer id;

    /**
     * 文章缩略图
     */
    private String cover;

    /**
     * 标题
     */
    private String title;

    /**
     * 发表时间
     */
    private Date updateTime;

    /**
     * 文章分类id
     */
    private Integer categoryId;

    /**
     * 文章分类名
     */
    private String categoryName;

    /**
     * 文章标签
     */
    private List<TagSimpleDTO> tagDTOList;

}
