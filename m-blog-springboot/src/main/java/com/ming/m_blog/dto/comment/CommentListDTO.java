package com.ming.m_blog.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 后台评论列表查询结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentListDTO {

    /**
     * id
     */
    private Integer id;

    /**
     *用户头像
     */
    private String avatar;

    /**
     * 评论用户昵称
     */
    private String nickname;

    /**
     * 评论对象昵称
     */
    private String replyNickname;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论时间
     */
    private Date commentTime;

    /**
     * 评论状态
     */
    private Integer isReview;

    /**
     * 评论来源 1文章 2友链 3说说
     */
    private Integer type;

}
