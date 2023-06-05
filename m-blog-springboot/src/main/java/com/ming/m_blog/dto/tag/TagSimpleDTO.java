package com.ming.m_blog.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 简单标签信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagSimpleDTO {

    /**
     * 标签id
     */
    private Integer id;

    /**
     * 标签名称
     */
    private String tagName;

}
