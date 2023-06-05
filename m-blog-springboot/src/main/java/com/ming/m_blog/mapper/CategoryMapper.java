package com.ming.m_blog.mapper;

import com.ming.m_blog.dto.category.CategoryDTO;
import com.ming.m_blog.dto.category.CategoryListDTO;
import com.ming.m_blog.pojo.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  Mapper 分类接口
 *
 * @author Ming
 * @since 2022-08-11
 */
@Repository
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 分页查询分类信息
     * @param pageNum   页码
     * @param pageSize  每页数量
     * @param keywords  查询信息
     * @return          查询结果
     */
    List<CategoryListDTO> selectCategoryList(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize, @Param("keywords") String keywords);

    /**
     * 前台查询分类列表
     * @return
     */
    List<CategoryDTO> listCategory();

}
