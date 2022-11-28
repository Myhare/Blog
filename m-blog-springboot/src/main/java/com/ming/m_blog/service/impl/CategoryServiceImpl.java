package com.ming.m_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ming.m_blog.dto.CategoryDTO;
import com.ming.m_blog.dto.CategoryListDTO;
import com.ming.m_blog.dto.CategorySimpleDTO;
import com.ming.m_blog.exception.ReRuntimeException;
import com.ming.m_blog.mapper.ArticleMapper;
import com.ming.m_blog.pojo.Article;
import com.ming.m_blog.pojo.Category;
import com.ming.m_blog.mapper.CategoryMapper;
import com.ming.m_blog.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.m_blog.utils.BeanCopyUtils;
import com.ming.m_blog.utils.UserUtils;
import com.ming.m_blog.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ming
 * @since 2022-08-11
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    // 分页查询分类信息
    @Override
    public List<CategoryListDTO> getCategoryList(int pageNum, int pageSize, String keywords) {
        pageNum = (pageNum-1)*pageSize;
        return categoryMapper.selectCategoryList(pageNum,pageSize,keywords);
    }

    // 查询所有分类信息
    @Override
    public List<CategorySimpleDTO> searchCategory(String keywords) {
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .like(Category::getCategoryName, keywords));
        return BeanCopyUtils.copyList(categoryList, CategorySimpleDTO.class);
    }

    // 查询所有分类信息
    @Override
    public List<CategorySimpleDTO> getAllCategory() {
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>());
        return BeanCopyUtils.copyList(categoryList,CategorySimpleDTO.class);
    }

    // 查询分类数量
    @Override
    public int getCateCount(String keywords) {
        return categoryMapper.selectCount(
                new LambdaQueryWrapper<Category>()
                        .like(Category::getCategoryName, keywords)
        );
    }

    // 手动添加分类
    @Override
    public int addCateGory(String categoryName) {
        Integer cateCount = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                .eq(Category::getCategoryName, categoryName));
        if (cateCount>0){
            throw new ReRuntimeException("该分类已经存在");
        }
        Category category = Category.builder()
                .categoryName(categoryName)
                .build();
        return categoryMapper.insert(category);
    }

    // 删除分类
    @Transactional
    @Override
    public int deleteCategory(List<Integer> cateIdList) {
        // 查询要删除的分类下面有没有文章，如果有，报错
        Integer count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .in(Article::getCategoryId, cateIdList));
        if (count>0){
            throw new ReRuntimeException("删除失败，分类下存在文章");
        }
        return categoryMapper.deleteBatchIds(cateIdList);
    }

    // 前台查询分类
    @Override
    public PageResult<CategoryDTO> listCategory() {
        List<CategoryDTO> listCategory = categoryMapper.listCategory();
        Integer categoryCount = categoryMapper.selectCount(null);
        return new PageResult<>(listCategory,categoryCount);
    }
}
