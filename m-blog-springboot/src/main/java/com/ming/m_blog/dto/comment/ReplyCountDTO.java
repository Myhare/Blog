package com.ming.m_blog.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 回复评论的数量
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCountDTO {
    /**
     * 评论id
     */
    private Integer commentId;

    /**
     * 回复评论数量
     */
    private Integer replyCount;

}
