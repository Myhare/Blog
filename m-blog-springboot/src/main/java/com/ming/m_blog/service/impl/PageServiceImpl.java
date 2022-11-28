package com.ming.m_blog.service.impl;

import com.ming.m_blog.enums.FilePathEnum;
import com.ming.m_blog.pojo.Page;
import com.ming.m_blog.mapper.PageMapper;
import com.ming.m_blog.service.PageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.m_blog.utils.FileUtils;
import com.ming.m_blog.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ming
 * @since 2022-11-08
 */
@Service
public class PageServiceImpl extends ServiceImpl<PageMapper, Page> implements PageService {

    @Autowired
    private PageMapper pageMapper;

    // 查询页面信息
    @Override
    public List<Page> getPageList() {
        return pageMapper.selectList(null);
    }

    // 修改页面信息
    @Override
    public int updatePage(Page page) {
        return pageMapper.updateById(page);
    }
}
