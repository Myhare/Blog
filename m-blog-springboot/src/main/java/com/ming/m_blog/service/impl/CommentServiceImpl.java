package com.ming.m_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ming.m_blog.constant.CommonConst;
import com.ming.m_blog.dto.*;
import com.ming.m_blog.pojo.Comment;
import com.ming.m_blog.mapper.CommentMapper;
import com.ming.m_blog.service.BlogService;
import com.ming.m_blog.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.m_blog.service.RedisService;
import com.ming.m_blog.utils.PageUtils;
import com.ming.m_blog.utils.UserUtils;
import com.ming.m_blog.vo.CommentsVO;
import com.ming.m_blog.vo.AdminCommentsVO;
import com.ming.m_blog.vo.PageResult;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ming.m_blog.constant.CommonConst.*;
import static com.ming.m_blog.constant.RedisPrefixConst.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ming
 * @since 2022-09-09
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private BlogService blogService;


    // 后台查询评论列表
    @Override
    public PageResult<CommentListDTO> getCommentList(AdminCommentsVO commentsVO) {
        // 查询所有的数量
        Integer commentCount = commentMapper.getAdminCommentCount(commentsVO);
        if (commentCount == 0){
            return new PageResult<>(null,0);
        }
        Integer pageNum = commentsVO.getPageNum();
        Integer pageSize = commentsVO.getPageSize();
        commentsVO.setPageNum((pageNum-1)*pageSize);        // 将页数转化成查询的具体下标
        // 查询评论列表
        List<CommentListDTO> commentList = commentMapper.getAdminCommentList(commentsVO);
        return new PageResult<>(commentList,commentCount);
    }

    // 批量删除评论列表
    @Override
    @Transactional(rollbackFor=Exception.class)
    public int delCommentList(List<Integer> commentIdList) {
        return commentMapper.deleteBatchIds(commentIdList);
    }

    // 彻底删除评论
    @Override
    public int reallyDelCommentList(List<Integer> commentIdList) {
        return commentMapper.reallyDelCommentList(commentIdList);
    }

    // 批量通过评论列表
    @Override
    @Transactional(rollbackFor=Exception.class)
    public boolean reviewCommentList(List<Integer> commentIdList) {
        List<Comment> commentList = commentIdList
                .stream()
                .map(id -> (
                        Comment.builder()
                                .id(id)
                                .isReview(1)
                                .build()
                ))
                .collect(Collectors.toList());
        return this.updateBatchById(commentList);
    }

    // 用户添加评论
    @Override
    public int addComments(CommentsVO commentsVO) {
        // 获取是否需要审核
        Integer isReview = blogService.getWebsiteConfig().getIsCommentReview();
        UserDetailDTO loginUser = UserUtils.getLoginUser();
        Comment comment = Comment.builder()
                .userId(loginUser.getUserId())
                .replyUserId(commentsVO.getReplyUserId())
                .topicId(commentsVO.getTopicId())
                .commentContent(commentsVO.getCommentContent())
                .parentId(commentsVO.getParentId())
                .type(commentsVO.getType())
                .isReview(isReview == TRUE ? FALSE : TRUE)
                .build();
        // TODO 可以添加邮箱通知用户功能
        return commentMapper.insert(comment);
    }

    // 查询主题下的评论列表
    @Override
    public PageResult<CommentDTO> getComments(CommentsVO commentsVO) {
        // 查询改主题下有多少评论
        Integer commentCount = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getTopicId, commentsVO.getTopicId())
                .eq(Comment::getIsReview, TRUE)
                .isNull(Comment::getParentId)
        );
        if (commentCount==0){
            return new PageResult<>();
        }
        // 分类查询评论数据
        // 从分页工具中获取分页信息
        List<CommentDTO> commentDTOList = commentMapper.getCommentDTO(commentsVO, PageUtils.getLimitCurrent(), PageUtils.getSize());
        // 获取评论列表的id
        List<Integer> commentIdList = commentDTOList.stream()
                .map(CommentDTO::getId)
                .collect(Collectors.toList());
        // 获取评论的子评论
        List<ReplyDTO> replyDTOList = commentMapper.getReplyDTO(commentIdList);
        // 获取评论回复的数量,因为前面查询的不是完整的回复
        List<ReplyCountDTO> replyCountList = commentMapper.getReplyCount(commentIdList);

        // 查询评论点赞数量集合(评论id:评论点赞数量)
        Map<String, Object> commentLikeCountMap = redisService.hGetAll(COMMENT_LIKE_COUNT);

        // 封装回复评论点赞量
        replyDTOList.forEach(item -> item.setLikeCount((Integer)commentLikeCountMap.getOrDefault(item.getId().toString(),0)));
        // 将回复评论通过parent进行分组
        Map<Integer, List<ReplyDTO>> replyMap =
                replyDTOList
                .stream()
                .collect(Collectors.groupingBy(ReplyDTO::getParentId));
        // 将回复数量通过parent分组
        Map<Integer, Integer> replyCountMap = replyCountList.stream()
                .collect(Collectors.toMap(ReplyCountDTO::getCommentId,ReplyCountDTO::getReplyCount));

        // 封装评论点赞量并拼装父评论和子评论
        commentDTOList.forEach(comment -> {
            comment.setLikeCount((Integer)commentLikeCountMap.getOrDefault(comment.getId().toString(),0));
            comment.setReplyDTOList(replyMap.get(comment.getId())); // 配置回复评论
            comment.setReplyCount(replyCountMap.get(comment.getId()));
        });

        return new PageResult<CommentDTO>(commentDTOList,commentCount);
    }

    // 获取评论的回复
    @Override
    public List<ReplyDTO> getReplyComments(Integer commentsId) {
        List<ReplyDTO> replyCommentList = commentMapper.getReplyByCommentId(commentsId, PageUtils.getLimitCurrent(), PageUtils.getSize());
        // 封装点赞量
        Map<String, Object> commentLikeCountMap = redisService.hGetAll(COMMENT_LIKE_COUNT);
        // 从redis读取点赞量
        replyCommentList.forEach(replyComment -> {
            replyComment.setLikeCount((Integer) commentLikeCountMap.getOrDefault(replyComment.getId().toString(),0));
        });
        return replyCommentList;
    }

    // 评论点赞功能
    @Override
    public void commentLike(Integer commentId) {
        Integer loginUserId = UserUtils.getLoginUserId();
        String userIsLikeKey = COMMENT_USER_LIKE + loginUserId;    // 用户点赞集合key
        // 判断用户点赞的评论里面有没有当前评论
        if (redisService.sIsMember(userIsLikeKey,commentId)){
            // 如果有，取消当前点赞
            redisService.sRemove(userIsLikeKey,commentId);
            // 对评论的点赞数量进行修改
            redisService.hDecr(COMMENT_LIKE_COUNT,commentId.toString(), 1L);
        }else {
            // 对当前评论点赞
            redisService.sAdd(userIsLikeKey,commentId);
            // 评论点赞数量加一
            redisService.hIncr(COMMENT_LIKE_COUNT,commentId.toString(),1L);
        }
    }

    // 恢复评论
    @Override
    public int restoreComment(List<Integer> commentIdList) {
        return commentMapper.restoreCommentList(commentIdList);
    }
}
