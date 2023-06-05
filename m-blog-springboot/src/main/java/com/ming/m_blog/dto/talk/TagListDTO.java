package com.ming.m_blog.dto.talk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 查询标签信息数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagListDTO {

    /**
     * 标签id
     */
    private int tagId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 发布的文章数量
     */
    private int articleCount;

    /**
     * 更新时间
     */
    private Date updateTime;

}
