package com.ming.m_blog.controller;

import com.ming.m_blog.annotation.OptLog;
import com.ming.m_blog.constant.OptTypeConstant;
import com.ming.m_blog.constant.PowerConst;
import com.ming.m_blog.dto.friendLink.FriendLinkBackDTO;
import com.ming.m_blog.dto.friendLink.FriendLinkDTO;
import com.ming.m_blog.pojo.FriendLink;
import com.ming.m_blog.service.FriendLinkService;
import com.ming.m_blog.vo.FriendLinkVO;
import com.ming.m_blog.vo.PageResult;
import com.ming.m_blog.vo.QueryInfoVO;
import com.ming.m_blog.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 友链控制器
 */
@Api(tags = "友链模块")
@RestController
public class FriendLinkController {

    @Autowired
    private FriendLinkService friendLinkService;

    /**
     * 查看友链列表
     */
    @ApiOperation("查看后台友链列表")
    @GetMapping("/admin/links")
    public ResponseResult<PageResult<FriendLinkBackDTO>> friendLinkList(QueryInfoVO queryInfoVO){
        return ResponseResult.ok(friendLinkService.listLinkBack(queryInfoVO));
    }

    /**
     * 添加或修改友链
     */
    @PreAuthorize("hasAuthority('sys:admin')")
    @OptLog(optType = OptTypeConstant.UPDATE)
    @ApiOperation("添加或修改友链")
    @PostMapping("/admin/links")
    public ResponseResult<?> saveOrUpdateLink(@RequestBody FriendLinkVO friendLinkVO){
        friendLinkService.saveOrUpdateLink(friendLinkVO);
        return ResponseResult.ok();
    }

    /**
     * 删除友链
     */
    @PreAuthorize("hasAuthority('sys:admin')")
    @OptLog(optType = OptTypeConstant.REMOVE)
    @ApiOperation("删除友链")
    @DeleteMapping("/admin/links")
    public ResponseResult<?> deleteLink(@RequestBody List<Integer> linkIdList){
        friendLinkService.removeByIds(linkIdList);
        return ResponseResult.ok();
    }

    /**
     * 查看前台友链列表
     */
    @ApiOperation("查询友链列表")
    @GetMapping("/links")
    public ResponseResult<List<FriendLinkDTO>> linkList(){
        return ResponseResult.ok(friendLinkService.listLink());
    }
}
