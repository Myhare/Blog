package com.ming.m_blog.controller;


import com.ming.m_blog.dto.category.CategoryDTO;
import com.ming.m_blog.dto.category.CategoryListDTO;
import com.ming.m_blog.dto.category.CategorySimpleDTO;
import com.ming.m_blog.service.CategoryService;
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
 *  分类控制器
 *
 * @author Ming
 * @since 2022-08-11
 */
@RestController
@Api(tags = "分类模块")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation("查询分类列表")
    @GetMapping("/categories")
    public ResponseResult<PageResult<CategoryDTO>> getCategory(){
        return ResponseResult.ok(categoryService.listCategory());
    }

    /**
     * 分页查询文章分类信息
     * @param queryInfoVO 查询参数
     * @return            返回信息
     */
    @ApiOperation("分页查询所有分类")
    @GetMapping("/getCateList")
    public ResponseResult<PageResult<CategoryListDTO>> selectCategoryList(QueryInfoVO queryInfoVO){
        List<CategoryListDTO> categoryList = categoryService.getCategoryList(queryInfoVO.getPageNum(), queryInfoVO.getPageSize(), queryInfoVO.getKeywords());
        int cateCount = categoryService.getCateCount(queryInfoVO.getKeywords());
        PageResult<CategoryListDTO> pageResult = new PageResult<>(categoryList, cateCount);
        return ResponseResult.ok(pageResult);
    }

    /**
     * 查询所有分类信息
     * @return  查询结果
     */
    @ApiOperation("模糊查询分类信息")
    @GetMapping("/searchCateList")
    public ResponseResult<List<CategorySimpleDTO>> searchCateList(String keywords){
        List<CategorySimpleDTO> categorySimpleDTOList = categoryService.searchCategory(keywords);
        return ResponseResult.ok(categorySimpleDTOList);
    }

    @ApiOperation("查询所有分类信息")
    @GetMapping("/getAllCateList")
    public ResponseResult<List<CategorySimpleDTO>> getAllCateList(){
        List<CategorySimpleDTO> allCategory = categoryService.getAllCategory();
        return ResponseResult.ok(allCategory);
    }

    /**
     * 添加一个分类
     * @param categoryName 分类名称
     * @return 返回结果
     */
    @PreAuthorize("hasAuthority('sys:admin')")
    @ApiOperation("添加分类")
    @PostMapping("/addCategory/{categoryName}")
    public ResponseResult<?> addCategory(@PathVariable("categoryName")String categoryName){
        int i = categoryService.addCateGory(categoryName);
        if (i>0){
            return ResponseResult.ok();
        }else {
            return ResponseResult.fail();
        }
    }

    @PreAuthorize("hasAuthority('sys:admin')")
    @ApiOperation("删除分类")
    @PostMapping("/deleteCate")
    public ResponseResult<String> deleteCate(@RequestBody List<Integer> cateIdList){
        categoryService.deleteCategory(cateIdList);
        return ResponseResult.ok();
    }
}

