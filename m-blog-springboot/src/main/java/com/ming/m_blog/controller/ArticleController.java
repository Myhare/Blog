package com.ming.m_blog.controller;


import com.ming.m_blog.dto.*;
import com.ming.m_blog.service.ArticleService;
import com.ming.m_blog.service.FileService;
import com.ming.m_blog.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 博客信息控制器
 *
 * @author Ming
 * @since 2022-08-11
 */
@Api(tags = "博客模块")
@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private FileService fileService;

    @ApiOperation("查询首页博客列表")
    @GetMapping("/articles")
    public ResponseResult<List<HomeArticleDTO>> getArticles(){
        return ResponseResult.ok(articleService.getHomeArticles());
    }

    @ApiOperation("查询博客详细信息")
    @GetMapping("/articles/{articleId}")
    public ResponseResult<ArticleDTO> getArticle(@PathVariable Integer articleId){
        return ResponseResult.ok(articleService.getArticle(articleId));
    }

    @PreAuthorize("hasAuthority('sys:admin')")
    @ApiOperation("添加文章")
    @PostMapping("/addArticle")
    public ResponseResult<String> addArticle(@RequestBody ArticleAddVO articleAddVO){
        boolean flag = articleService.addArticle(articleAddVO);
        if (flag){
            return ResponseResult.ok();
        }else {
            return ResponseResult.fail();
        }
    }

    @ApiOperation(value = "博客文章图片上传")
    @PreAuthorize("hasAuthority('sys:admin')")
    @PostMapping("/articleFile")
    public ResponseResult<String> article(MultipartFile file){
        String fileUrl = fileService.articleFileUpload(file);
        return ResponseResult.ok(fileUrl);
    }

    // 查询文章列表
    @ApiOperation("后台查询博客列表")
    @GetMapping("/admin/articles")
    public ResponseResult<PageResult<ArticleListInfoDTO>> getArticlesList(AdminArticlesVO adminArticlesVO){
        PageResult<ArticleListInfoDTO> pageResult = articleService.getArticleList(adminArticlesVO);
        return ResponseResult.ok(pageResult);
    }

    // 删除博客
    @ApiOperation("删除博客")
    @PreAuthorize("hasAuthority('sys:admin')")
    @PostMapping("/admin/deleteArticle")
    public ResponseResult<String> deleteArticle(@RequestBody List<Integer> articleIdList){
        // System.out.println("成功接收到数据");
        // System.out.println(articleIdList);
        articleService.deleteArticle(articleIdList);
        return ResponseResult.ok();
    }

    // 修改文章置顶情况
    @ApiOperation("修改文章置顶情况")
    @PreAuthorize("hasAuthority('sys:admin')")
    @PostMapping("/admin/changeTop")
    public ResponseResult<String> changeTop(@RequestBody ChangeArticleTopVO changeArticleTopVO){
        articleService.changeArticleTop(changeArticleTopVO);
        return ResponseResult.ok();
    }

    // 通过id查询博客
    @ApiOperation("通过id查询博客")
    @GetMapping("/selectArticle")
    public ResponseResult<ArticleAddVO> selectArticleById(Integer articleId){
        ArticleAddVO article = articleService.getArticleById(articleId);
        return ResponseResult.ok(article);
    }

    // 条件查询文章列表
    @ApiOperation("条件查询文章列表")
    @GetMapping("/articles/condition")
    public ResponseResult<ArticlePreviewListDTO> conditionArticle(ArticleListConditionVO conditionVO){
        return ResponseResult.ok(articleService.conditionArticle(conditionVO));
    }

    // 查询文章归档
    @ApiOperation("查询文章归档")
    @GetMapping("/articles/archives")
    public ResponseResult<PageResult<ArchiveDTO>> getArchive(Integer current){
        return ResponseResult.ok(articleService.getArchive(current));
    }

}
