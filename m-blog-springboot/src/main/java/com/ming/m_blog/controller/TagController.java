package com.ming.m_blog.controller;


import com.ming.m_blog.dto.TagListDTO;
import com.ming.m_blog.dto.TagSimpleDTO;
import com.ming.m_blog.service.TagService;
import com.ming.m_blog.vo.PageResult;
import com.ming.m_blog.vo.QueryInfoVO;
import com.ming.m_blog.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  标签控制器
 *
 * @author Ming
 * @since 2022-08-12
 */
@RestController
@Api(tags = "标签模块")
public class TagController {

    @Autowired
    private TagService tagService;

    @ApiOperation("查询标签列表")
    @GetMapping("/tags")
    public ResponseResult<PageResult<TagSimpleDTO>> getTags(){
        return ResponseResult.ok(tagService.getTags());
    }

    @ApiOperation("添加标签")
    @PostMapping("/addTag/{tagName}")
    public ResponseResult<String> addTag(@PathVariable("tagName")String tagName){
        tagService.addTag(tagName);
        return ResponseResult.ok();
    }

    @ApiOperation("分页查询标签")
    @GetMapping("/getTabList")
    public ResponseResult<PageResult<TagListDTO>> getTagList(QueryInfoVO queryInfoVO){
        List<TagListDTO> tagList = tagService.getTagList(queryInfoVO.getPageNum(), queryInfoVO.getPageSize(), queryInfoVO.getKeywords());
        int tagCount = tagService.getTagCount(queryInfoVO.getKeywords());
        PageResult<TagListDTO> pageResult = new PageResult<>(tagList, tagCount);
        return ResponseResult.ok(pageResult);
    }

    @PreAuthorize("hasAuthority('sys:admin')")
    @ApiOperation("删除标签")
    @PostMapping("/deleteTag")
    public ResponseResult<String> delTag(@RequestBody List<Integer> tagIdList){
        tagService.delTag(tagIdList);
        return ResponseResult.ok();
    }

    @ApiOperation("查询所有标签")
    @GetMapping("/getAllTagName")
    public ResponseResult<List<TagSimpleDTO>> getAllTagName(){
        List<TagSimpleDTO> tagSimpleDTOList = tagService.getAllTagName();
        return ResponseResult.ok(tagSimpleDTOList);
    }

    @ApiOperation("模糊查询标签")
    @GetMapping("/searchTagList")
    public ResponseResult<List<String>> searchTagList(String keywords){
        List<String> tagNameList = tagService.getTagByKeywords(keywords);
        return ResponseResult.ok(tagNameList);
    }
}

