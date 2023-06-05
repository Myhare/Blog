package com.ming.m_blog.dto.blogInfo;

import com.ming.m_blog.dto.category.CategoryDTO;
import com.ming.m_blog.dto.tag.TagSimpleDTO;
import com.ming.m_blog.dto.article.ArticleRankDTO;
import com.ming.m_blog.dto.article.ArticleStatisticsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 博客后台统计信息
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogBackStatisticsDTO {
    /**
     * 访问量
     */
    private Integer viewsCount;

    /**
     * 留言量
     */
    private Integer messageCount;

    /**
     * 用户量
     */
    private Integer userCount;

    /**
     * 文章量
     */
    private Integer articleCount;

    /**
     * 分类统计
     */
    private List<CategoryDTO> categoryDTOList;

    /**
     * 标签列表
     */
    private List<TagSimpleDTO> tagDTOList;

    /**
     * 文章统计列表
     */
    private List<ArticleStatisticsDTO> articleStatisticsList;

    /**
     * 一周用户量集合
     */
    private List<UniqueViewDTO> uniqueViewDTOList;

    /**
     * 文章浏览量排行
     */
    private List<ArticleRankDTO> articleRankDTOList;

}
