package com.ming.m_blog.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 爬取到的图片
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchPictureDTO {

    /**
     * 图片标题
     */
    private String title;

    /**
     * 图片访问url
     */
    private String url;
}
