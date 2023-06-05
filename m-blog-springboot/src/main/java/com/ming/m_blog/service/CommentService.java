package com.ming.m_blog.service;

import com.ming.m_blog.dto.comment.CommentDTO;
import com.ming.m_blog.dto.comment.CommentListDTO;
import com.ming.m_blog.dto.comment.ReplyDTO;
import com.ming.m_blog.pojo.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.m_blog.vo.CommentsVO;
import com.ming.m_blog.vo.AdminCommentsVO;
import com.ming.m_blog.vo.PageResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Ming
 * @since 2022-09-09
 */
public interface CommentService extends IService<Comment> {

    /**
     * 后台查询评论列表
     * @param commentsVO    查询条件
     * @return              查询结果
     */
    PageResult<CommentListDTO> getCommentList(AdminCommentsVO commentsVO);

    /**
     * 批量删除评论列表
     * @param commentIdList 评论id列表
     * @return              删除结果
     */
    int delCommentList(List<Integer> commentIdList);

    /**
     * 彻底删除评论列表
     * @param commentIdList 评论id列表
     * @return              删除结果
     */
    int reallyDelCommentList(List<Integer> commentIdList);

    /**
     * 批量通过评论列表
     * @param commentIdList  评论id列表
     * @return               通过结果
     */
    boolean reviewCommentList(List<Integer> commentIdList);


    /**
     * 用户添加评论
     * @param commentsVO    评论信息
     * @return              影响行数
     */
    int addComments(CommentsVO commentsVO);

    /**
     * 获取主题下的一个评论列表
     * @param commentsVO 查询条件
     * @return           查询结果
     */
    PageResult<CommentDTO> getComments(CommentsVO commentsVO);

    /**
     * 获取评论的回复
     * @param commentsId    要查询回复的评论id
     * @return              查询到的回复的评论列表
     */
    List<ReplyDTO> getReplyComments(Integer commentsId);

    /**
     * 评论点赞
     * @param commentId 评论id
     */
    void commentLike(Integer commentId);

    /**
     * 批量恢复评论
     * @param commentIdList 评论id列表
     * @return              恢复结果
     */
    int restoreComment(List<Integer> commentIdList);
}
