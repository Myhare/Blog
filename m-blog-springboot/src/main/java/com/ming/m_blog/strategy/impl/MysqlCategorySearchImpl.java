package com.ming.m_blog.strategy.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ming.m_blog.constant.CommonConst;
import com.ming.m_blog.dto.search.CategorySearchDTO;
import com.ming.m_blog.mapper.CategoryMapper;
import com.ming.m_blog.pojo.Category;
import com.ming.m_blog.vo.SearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 本地搜素分类
 */
@Service("mysqlCategorySearchImpl")
public class MysqlCategorySearchImpl extends AbstractSearchStrategy {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 搜索本地分类
     * @param keyword 搜索关键词
     * @return        搜索结果
     */
    @Override
    public SearchVO search(String keyword) {
        // 判空
        if (!StringUtils.hasLength(keyword)){
            return new SearchVO();
        }

        // 搜索
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .like(Category::getCategoryName, keyword)
        );

        // 转换并且设置高亮
        List<CategorySearchDTO> categorySearchDTOList = categoryList.stream()
                .map(category -> {
                    String categoryName = category.getCategoryName();
                    // 获取第一次出现的位置
                    categoryName = categoryName.replaceAll(keyword, CommonConst.PRE_TAG + keyword + CommonConst.POST_TAG);
                    return CategorySearchDTO.builder()
                            .id(category.getId())
                            .categoryName(categoryName)
                            .build();
                }).collect(Collectors.toList());

        return SearchVO.builder()
                .categoryList(categorySearchDTOList)
                .build();
    }
}
