package com.ming.m_blog.controller;


import com.ming.m_blog.pojo.Page;
import com.ming.m_blog.service.PageService;
import com.ming.m_blog.vo.PageResult;
import com.ming.m_blog.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public ResponseResult<List<Page>> getPageList(){
        return ResponseResult.ok(pageService.getPageList());
    }

    @ApiOperation("修改页面信息")
    @PostMapping("/admin/pages")
    public ResponseResult<String> updatePages(@RequestBody Page page){
        pageService.updatePage(page);
        return ResponseResult.ok();
    }

}

