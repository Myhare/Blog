package com.ming.m_blog.service;

import com.ming.m_blog.dto.blogInfo.BlogBackStatisticsDTO;
import com.ming.m_blog.dto.blogInfo.BlogInfoDTO;
import com.ming.m_blog.vo.QueryInfoVO;
import com.ming.m_blog.vo.SearchVO;
import com.ming.m_blog.vo.WebsiteConfigVO;

/**
 * 博客服务
 */
public interface BlogService {

    /**
     * 获取博客基本信息
     * @return   获取到的信息
     */
    BlogInfoDTO getBlogInfo();

    /**
     * 获取网站基本信息
     */
    WebsiteConfigVO getWebsiteConfig();

    /**
     * 查询博客后台统计信息
     */
    BlogBackStatisticsDTO getBackStatistics();

    /**
     * 更新网站基本信息
     * @param websiteConfigVO 要更新的信息
     */
    void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO);

    /**
     * 更新用户登录信息
     */
    void report();

    /**
     * 全局搜索文章
     * @param queryInfoVO
     * @return
     */
    SearchVO blogSearchList(QueryInfoVO queryInfoVO);

}
