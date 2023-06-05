package com.ming.m_blog.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论数量dto
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentCountDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 评论数量
     */
    private Integer commentCount;
}
