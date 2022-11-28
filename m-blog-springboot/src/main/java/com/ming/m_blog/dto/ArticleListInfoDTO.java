package com.ming.m_blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页查询文章列表对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleListInfoDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 封面url
     */
    private String cover;

    /**
     * 标题
     */
    private String title;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 标签名称列表
     */
    private List<String> tagNameList;

    /**
     * 浏览量
     */
    private Integer pageViews;

    /**
     * 点赞量
     */
    private Integer pageLikes;

    /**
     * 文章类型
     */
    private Integer type;

    /**
     * 是否置顶
     */
    private Integer isTop;
}
