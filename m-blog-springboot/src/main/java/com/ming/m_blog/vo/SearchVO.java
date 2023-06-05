package com.ming.m_blog.vo;

import com.ming.m_blog.dto.search.SearchPictureDTO;
import com.ming.m_blog.dto.search.ArticleSearchDTO;
import com.ming.m_blog.dto.search.CategorySearchDTO;
import com.ming.m_blog.dto.search.TagSearchDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchVO {

    /**
     * 搜索的文章列表
     */
    private List<ArticleSearchDTO> articleList;

    /**
     * 搜索分类列表
     */
    private List<CategorySearchDTO> categoryList;

    /**
     * 搜索标签列表
     */
    private List<TagSearchDTO> tagList;


    /**
     * 搜索图片列表
     */
    private List<SearchPictureDTO> pictureList;
}
