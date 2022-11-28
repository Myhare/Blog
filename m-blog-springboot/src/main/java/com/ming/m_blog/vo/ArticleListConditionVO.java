package com.ming.m_blog.vo;

import lombok.Data;
import lombok.Setter;

/**
 * 查询文章列表参数
 */
@Data
public class ArticleListConditionVO {

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 标签id
     */

    private Integer tagId;

    /**
     * 当前查询次数
     */
    private Integer current;

}
