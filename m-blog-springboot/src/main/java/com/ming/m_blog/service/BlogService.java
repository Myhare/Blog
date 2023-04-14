package com.ming.m_blog.service;

import com.ming.m_blog.dto.BlogBackStatisticsDTO;
import com.ming.m_blog.dto.BlogInfoDTO;
import com.ming.m_blog.vo.WebsiteConfigVO;
import org.springframework.web.multipart.MultipartFile;

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

}
