package com.ming.m_blog.service;

import com.ming.m_blog.pojo.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.m_blog.vo.PageResult;
import com.ming.m_blog.vo.PageVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 网站页面服务类
 *
 * @author Ming
 * @since 2022-11-08
 */
public interface PageService extends IService<Page> {

    /**
     * 获取所有页面信息
     * @return 页面信息
     * @return 查询结果
     */
    List<PageVO> getPageList();

    /**
     * 修改页面信息
     * @param page 页面信息
     * @return     修改结果
     */
    void saveOrUpdatePage(PageVO page);

    /**
     * 删除页面
     * @param pageId 页面id
     */
    void deletePage(Integer pageId);
}
