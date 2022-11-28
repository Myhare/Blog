package com.ming.m_blog.dto;

import com.ming.m_blog.pojo.Page;
import com.ming.m_blog.vo.WebsiteConfigVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 博客信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogInfoDTO {

    /**
     * 文章数量
     */
    private Integer articleCount;

    /**
     * 分类数量
     */
    private Integer categoryCount;

    /**
     * 标签数量
     */
    private Integer tagCount;

    /**
     * 封面列表
     */
    private List<Page> pageList;

    /**
     * 网站配置
     */
    private WebsiteConfigVO websiteConfig;

    /**
     * 访问量
     */
    private String viewsCount;

}
