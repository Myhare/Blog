package com.ming.m_blog.controller;


import com.ming.m_blog.annotation.OptLog;
import com.ming.m_blog.constant.OptTypeConstant;
import com.ming.m_blog.constant.RedisPrefixConst;
import com.ming.m_blog.pojo.Page;
import com.ming.m_blog.service.PageService;
import com.ming.m_blog.service.RedisService;
import com.ming.m_blog.vo.PageResult;
import com.ming.m_blog.vo.PageVO;
import com.ming.m_blog.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

/**
 * 页面控制器
 *
 * @author Ming
 * @since 2022-11-08
 */
@Api(tags = "页面模块")
@RestController
public class PageController {

    @Autowired
    private PageService pageService;


    @ApiOperation("查询页面列表")
    @GetMapping("/admin/page/list")
    public ResponseResult<List<PageVO>> getPageList(){
        return ResponseResult.ok(pageService.getPageList());
    }

    @OptLog(optType = OptTypeConstant.UPDATE)
    @ApiOperation("修改页面信息")
    @PreAuthorize("hasAuthority('sys:admin')")
    @PostMapping("/admin/pages")
    public ResponseResult<String> updatePages(@RequestBody PageVO page){
        pageService.saveOrUpdatePage(page);
        return ResponseResult.ok();
    }

    @OptLog(optType = OptTypeConstant.REMOVE)
    @ApiOperation("删除页面")
    @PreAuthorize("hasAuthority('sys:admin')")
    @DeleteMapping("/admin/pages/{pageId}")
    public ResponseResult<?> deletePage(@PathVariable("pageId") Integer pageId) {
        pageService.deletePage(pageId);
        return ResponseResult.ok();
    }
}

