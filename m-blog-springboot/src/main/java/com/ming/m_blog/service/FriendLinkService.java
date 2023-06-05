package com.ming.m_blog.service;

import com.ming.m_blog.dto.friendLink.FriendLinkBackDTO;
import com.ming.m_blog.dto.friendLink.FriendLinkDTO;
import com.ming.m_blog.pojo.FriendLink;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.m_blog.vo.FriendLinkVO;
import com.ming.m_blog.vo.PageResult;
import com.ming.m_blog.vo.QueryInfoVO;

import java.util.List;

/**
* @author 86135
* @description 针对表【friend_link】的数据库操作Service
* @createDate 2023-06-01 08:35:45
*/
public interface FriendLinkService extends IService<FriendLink> {

    /**
     * 后台查询友链列表
     * @param queryInfoVO 查询条件
     * @return            查询结果
     */
    PageResult<FriendLinkBackDTO> listLinkBack(QueryInfoVO queryInfoVO);

    /**
     * 添加或修改友链
     * @param friendLinkVO 前端友链
     */
    void saveOrUpdateLink(FriendLinkVO friendLinkVO);

    /**
     * 前台查询友链列表
     * @return 查询结果
     */
    List<FriendLinkDTO> listLink();
}
