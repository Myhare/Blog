package com.ming.m_blog;

import com.ming.m_blog.dto.CommentDTO;
import com.ming.m_blog.dto.CommentListDTO;
import com.ming.m_blog.dto.ReplyDTO;
import com.ming.m_blog.mapper.CommentMapper;
import com.ming.m_blog.service.CommentService;
import com.ming.m_blog.vo.AdminCommentsVO;
import com.ming.m_blog.vo.CommentsVO;
import com.ming.m_blog.vo.PageResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CommentTest {

    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentMapper commentMapper;

    // 后台查询评论列表
    @Test
    void getCommentList(){
        AdminCommentsVO commentsVO = AdminCommentsVO.builder()
                .type(1)
                .review(0)
                .pageNum(1)
                .pageSize(2)
                .keywords("")
                .isDelete(0)
                .build();
        PageResult<CommentListDTO> commentList = commentService.getCommentList(commentsVO);
        System.out.println(commentList);
    }

    // 查询评论的子评论
    @Test
    void getReply(){
        CommentsVO commentsVO = new CommentsVO(1, 17, "", 0, 1);
        PageResult<CommentDTO> comments = commentService.getComments(commentsVO);
        System.out.println(comments);
    }

    // 查看评论下面的具体评论
    @Test
    void getReplyComments(){
        List<ReplyDTO> replyByCommentId = commentMapper.getReplyByCommentId(2, Long.parseLong("0"), Long.parseLong("5"));
        System.out.println(replyByCommentId);
    }

}
