package com.ming.m_blog.dto.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 推荐文章对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleRecommendDTO {

    /**
     * id
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
     * 创建时间
     */
    private Date updateTime;

}
