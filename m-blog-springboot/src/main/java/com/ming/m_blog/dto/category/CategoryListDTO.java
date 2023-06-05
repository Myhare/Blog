package com.ming.m_blog.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 查询分类信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryListDTO {

    /**
     * 分类id
     */
    private int categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 发布的文章数量
     */
    private int articleCount;

    /**
     * 更新时间
     */
    private Date updateTime;
}
