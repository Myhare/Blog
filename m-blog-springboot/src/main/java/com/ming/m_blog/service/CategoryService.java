package com.ming.m_blog.service;

import com.ming.m_blog.dto.CategoryDTO;
import com.ming.m_blog.dto.CategoryListDTO;
import com.ming.m_blog.dto.CategorySimpleDTO;
import com.ming.m_blog.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.m_blog.vo.PageResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Ming
 * @since 2022-08-11
 */
public interface CategoryService extends IService<Category> {


    /**
     * 分页查询分类列表
     * @param pageNum    页数
     * @param pageSize   一页多少数据
     * @param keywords   查询内容
     * @return
     */
    List<CategoryListDTO> getCategoryList(int pageNum, int pageSize, String keywords);

    /**
     * 模糊查询分类信息
     * @return 查询结果
     */
    List<CategorySimpleDTO> searchCategory(String keywords);

    /**
     * 查询所有分类信息
     * @return 查询结果
     */
    List<CategorySimpleDTO> getAllCategory();

    /**
     * 查询所有的分类数量
     * @param keywords 查询条件
     * @return 查询到的分类数量
     */
    int getCateCount(String keywords);

    /**
     * 添加分类
     * @param categoryName 分类名称
     * @return 分类影响的行数
     */
    int addCateGory(String categoryName);

    /**
     * 删除分裂信息
     * @param cateIdList 要删除的分类id列表
     * @return           影响行数
     */
    int deleteCategory(List<Integer> cateIdList);

    /**
     * 查询前台分类
     */
    PageResult<CategoryDTO> listCategory();

}
