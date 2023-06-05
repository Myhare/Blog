package com.ming.m_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.m_blog.dto.friendLink.FriendLinkBackDTO;
import com.ming.m_blog.dto.friendLink.FriendLinkDTO;
import com.ming.m_blog.pojo.FriendLink;
import com.ming.m_blog.service.FriendLinkService;
import com.ming.m_blog.mapper.FriendLinkMapper;
import com.ming.m_blog.utils.BeanCopyUtils;
import com.ming.m_blog.utils.PageUtils;
import com.ming.m_blog.vo.FriendLinkVO;
import com.ming.m_blog.vo.PageResult;
import com.ming.m_blog.vo.QueryInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 86135
* @description 针对表【friend_link】的数据库操作Service实现
* @createDate 2023-06-01 08:35:45
*/
@Service
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink>
    implements FriendLinkService{

    @Autowired
    private FriendLinkMapper friendLinkMapper;

    // 后台查询友链列表
    @Override
    public PageResult<FriendLinkBackDTO> listLinkBack(QueryInfoVO queryInfoVO) {
        Page<FriendLink> page = new Page<>(PageUtils.getLimitCurrent(), PageUtils.getSize());
        Page<FriendLink> friendLinkPage = friendLinkMapper.selectPage(page, new LambdaQueryWrapper<FriendLink>()
                .like(StringUtils.isNotBlank(queryInfoVO.getKeywords()), FriendLink::getLinkName, queryInfoVO.getKeywords())
                .orderByDesc(FriendLink::getCreateTime)
        );
        // 将查询到的结果转换
        List<FriendLinkBackDTO> friendLinkBackDTOList = BeanCopyUtils.copyList(friendLinkPage.getRecords(), FriendLinkBackDTO.class);
        return new PageResult<FriendLinkBackDTO>(friendLinkBackDTOList, (int) friendLinkPage.getTotal());
    }

    // 添加或修改友链
    @Override
    public void saveOrUpdateLink(FriendLinkVO friendLinkVO) {
        this.saveOrUpdate(BeanCopyUtils.copyObject(friendLinkVO, FriendLink.class));
    }

    // 前台查询所有友链
    @Override
    public List<FriendLinkDTO> listLink() {
        List<FriendLink> friendLinkList = friendLinkMapper.selectList(new LambdaQueryWrapper<FriendLink>()
                .orderByDesc(FriendLink::getCreateTime)
        );
        return BeanCopyUtils.copyList(friendLinkList, FriendLinkDTO.class);
    }
}




