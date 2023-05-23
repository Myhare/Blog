package com.ming.m_blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.ming.m_blog.constant.RedisPrefixConst;
import com.ming.m_blog.enums.FilePathEnum;
import com.ming.m_blog.pojo.Page;
import com.ming.m_blog.mapper.PageMapper;
import com.ming.m_blog.service.PageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.m_blog.service.RedisService;
import com.ming.m_blog.utils.BeanCopyUtils;
import com.ming.m_blog.utils.FileUtils;
import com.ming.m_blog.vo.PageResult;
import com.ming.m_blog.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

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

    @Autowired
    private RedisService redisService;
    // 查询页面信息
    @Override
    public List<PageVO> getPageList() {
        List<PageVO> pageVOList;
        Object pageListObject = redisService.get(RedisPrefixConst.PAGE_COVER);
        if (Objects.nonNull(pageListObject)){
            pageVOList = JSON.parseArray(pageListObject.toString(),PageVO.class);
        }else {
            pageVOList = BeanCopyUtils.copyList(pageMapper.selectList(null), PageVO.class);
            redisService.set(RedisPrefixConst.PAGE_COVER, JSON.toJSONString(pageVOList));
        }
        return pageVOList;
    }

    // 修改页面信息
    @Override
    public void saveOrUpdatePage(PageVO pageVO) {
        Page page = BeanCopyUtils.copyObject(pageVO, Page.class);
        this.saveOrUpdate(page);
        // 删除页面缓存
        redisService.del(RedisPrefixConst.PAGE_COVER);
    }

    // 删除页面
    @Override
    public void deletePage(Integer pageId) {
        pageMapper.deleteById(pageId);
        redisService.del(RedisPrefixConst.PAGE_COVER);
    }
}
