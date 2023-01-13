package com.ming.m_blog.controller;


import com.ming.m_blog.dto.CommentDTO;
import com.ming.m_blog.dto.CommentListDTO;
import com.ming.m_blog.dto.ReplyDTO;
import com.ming.m_blog.annotation.AccessLimit;
import com.ming.m_blog.service.CommentService;
import com.ming.m_blog.vo.CommentsVO;
import com.ming.m_blog.vo.AdminCommentsVO;
import com.ming.m_blog.vo.PageResult;
import com.ming.m_blog.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 *
 * 评论管理
 *
 * @author Ming
 * @since 2022-09-09
 */
@Api(tags = "评论模块")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ApiOperation(value = "后台查询评论列表")
    @GetMapping("/admin/comments")
    public ResponseResult<PageResult<CommentListDTO>> getComments(AdminCommentsVO commentsVO){
        return ResponseResult.ok(commentService.getCommentList(commentsVO));
    }

    // 删除/批量删除评论
    @ApiOperation(value = "后台删除评论")
    @PostMapping("/admin/delComment")
    @PreAuthorize("hasAuthority('sys:admin')")
    public ResponseResult<String> delComment(@RequestBody List<Integer> commentIdList){
        commentService.delCommentList(commentIdList);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "彻底删除评论")
    @PostMapping("/admin/reallyDelComment")
    @PreAuthorize("hasAuthority('sys:admin')")
    public ResponseResult<String> reallyDelComment(@RequestBody List<Integer> commentIdList){
        commentService.reallyDelCommentList(commentIdList);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "恢复删除的评论")
    @PostMapping("/admin/restoreComment")
    @PreAuthorize("hasAuthority('sys:admin')")
    public ResponseResult<String> restoreComment(@RequestBody List<Integer> commentIdList){
        commentService.restoreComment(commentIdList);
        return ResponseResult.ok();
    }

    // 通过博客审核
    @ApiOperation(value = "审核评论")
    @PostMapping("/admin/reviewCom")
    @PreAuthorize("hasAuthority('sys:admin')")
    public ResponseResult<String> reviewComment(@RequestBody List<Integer> commentIdList){
        commentService.reviewCommentList(commentIdList);
        return ResponseResult.ok();
    }

    // 添加评论
    @ApiOperation(value = "添加评论")
    @PostMapping("/comments")
    public ResponseResult<String> addComments(@Valid @RequestBody CommentsVO commentsVO){
        commentService.addComments(commentsVO);
        return ResponseResult.ok();
    }

    // 前台获取评论列表
    @ApiOperation(value = "前台查询评论列表")
    @GetMapping("/comments")
    public ResponseResult<PageResult<CommentDTO>> getComments(CommentsVO commentsVO){
        return ResponseResult.ok(commentService.getComments(commentsVO));
    }

    // 获取指定评论下的回复
    @ApiOperation(value = "获取评论下指定的回复")
    @ApiImplicitParam(name = "commentId",value = "评论id",required = true,dataType = "Integer")
    @GetMapping("/comments/{commentId}/replies")
    public ResponseResult<List<ReplyDTO>> getReplyComments(@PathVariable("commentId")Integer commentId){
        return ResponseResult.ok(commentService.getReplyComments(commentId));
    }

    // 评论点赞功能
    @AccessLimit(seconds = 1, maxCount = 2)
    @ApiOperation(value = "评论点赞")
    @ApiImplicitParam(name = "commentId",value = "评论id",required = true,dataType = "Integer")
    @PostMapping("/comments/{commentId}/like")
    public ResponseResult<?> commentLike(@PathVariable("commentId")Integer commentId){
        commentService.commentLike(commentId);
        return ResponseResult.ok();
    }

}

