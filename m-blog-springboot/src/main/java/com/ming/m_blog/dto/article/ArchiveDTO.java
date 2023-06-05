package com.ming.m_blog.dto.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 查询文章归档列表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArchiveDTO {

    /**
     * 文章id
     */
    private Integer id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 创建时间
     */
    private Date createTime;

}
