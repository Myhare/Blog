package com.ming.m_blog.mapper;

import com.ming.m_blog.dto.CommentDTO;
import com.ming.m_blog.dto.CommentListDTO;
import com.ming.m_blog.dto.ReplyCountDTO;
import com.ming.m_blog.dto.ReplyDTO;
import com.ming.m_blog.pojo.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ming.m_blog.vo.AdminCommentsVO;
import com.ming.m_blog.vo.CommentsVO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Ming
 * @since 2022-09-09
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 后台查询博客数量
     * @param commentsVO 查询条件
     * @return           查询结果数量
     */
    Integer getAdminCommentCount(@Param("commentsVO")AdminCommentsVO commentsVO);

    /**
     * 后台查询评论列表
     * @param commentsVO 查询条件
     * @return           查询结果
     */
    List<CommentListDTO> getAdminCommentList(@Param("commentsVO")AdminCommentsVO commentsVO);

    /**
     * 查询主题下的评论
     * @param commentsVO 查询条件
     * @return           查询结果
     */
    List<CommentDTO> getCommentDTO(@Param("commentsVO")CommentsVO commentsVO,
                                   @Param("pageNum") Long pageNum,
                                   @Param("pageSize") Long pageSize);

    /**
     * 主题评论下面的子评论列表
     * @param commentIdList 评论id
     * @return              查询结果
     */
    List<ReplyDTO> getReplyDTO(@Param("commentIdList") List<Integer> commentIdList);

    /**
     * 批量查询评论回复的数量
     * @param commentIdList  评论id
     * @return               查询结果
     */
    List<ReplyCountDTO> getReplyCount(@Param("commentIdList") List<Integer> commentIdList);

    /**
     * 查询具体某个评论下的回复
     * @param commentId
     * @return
     */
    List<ReplyDTO> getReplyByCommentId(@Param("commentId") Integer commentId,
                                @Param("current")Long current,
                                @Param("size") Long size);

    /**
     * 批量删除id列表
     * @param commentIdList id列表
     * @return              删除结果
     */
    int reallyDelCommentList(@Param("commentIdList") List<Integer> commentIdList);

    /**
     * 批量恢复已删除评论
     * @param commentIdList 评论列表
     * @return              修改结果
     */
    int restoreCommentList(@Param("commentIdList") List<Integer> commentIdList);

}

















