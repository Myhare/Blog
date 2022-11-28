package com.ming.m_blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 博客首页展示文章
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HomeArticleDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 封面url
     */
    private String cover;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 标题
     */
    private String title;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 标签名称列表
     */
    private List<TagSimpleDTO> tagList;

    /**
     * 文章类型
     */
    private Integer type;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 更新时间
     */
    private Date updateTime;

}
