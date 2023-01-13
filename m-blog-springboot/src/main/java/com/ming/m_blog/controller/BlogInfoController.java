package com.ming.m_blog.controller;

import com.ming.m_blog.dto.BlogBackStatisticsDTO;
import com.ming.m_blog.dto.BlogInfoDTO;
import com.ming.m_blog.service.BlogService;
import com.ming.m_blog.service.FileService;
import com.ming.m_blog.vo.ResponseResult;
import com.ming.m_blog.vo.WebsiteConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "博客信息模块")
@RestController
public class BlogInfoController {

    @Autowired
    private BlogService blogService;

    @ApiOperation("获取博客基本信息")
    @GetMapping("/")
    public ResponseResult<BlogInfoDTO> getBlogInfo(){
        return ResponseResult.ok(blogService.getBlogInfo());
    }

    @ApiOperation(value = "更新用户信息")
    @PostMapping("/report")
    public ResponseResult<?> report(){
        blogService.report();
        return ResponseResult.ok();
    }

    @ApiOperation(value = "查看后台统计信息")
    @GetMapping("/admin")
    public ResponseResult<BlogBackStatisticsDTO> getBlogStatisticalInfo(){
        return ResponseResult.ok(blogService.getBackStatistics());
    }

    @ApiOperation("后台获取网站信息")
    @GetMapping("/admin/website/config")
    public ResponseResult<WebsiteConfigVO> getWebsiteInfo(){
        return ResponseResult.ok(blogService.getWebsiteConfig());
    }

    @PreAuthorize("hasAuthority('sys:admin')")
    @ApiOperation("修改网站基本信息")
    @PostMapping("/admin/website/config")
    public ResponseResult<?> updateWebsite(@RequestBody WebsiteConfigVO websiteConfigVO){
        blogService.updateWebsiteConfig(websiteConfigVO);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "配置网站图片")
    @PreAuthorize("hasAuthority('sys:admin')")
    @PostMapping("/admin/config/image")
    public ResponseResult<String> updateWebsiteImage(MultipartFile file){
        return ResponseResult.ok(blogService.websiteImageUpload(file));
    }
}
