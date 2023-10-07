package com.ming.m_blog.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.m_blog.constant.MQPrefixConst;
import com.ming.m_blog.dto.EmailSendDTO;
import com.ming.m_blog.dto.comment.CommentDTO;
import com.ming.m_blog.dto.comment.CommentListDTO;
import com.ming.m_blog.dto.comment.ReplyCountDTO;
import com.ming.m_blog.dto.comment.ReplyDTO;
import com.ming.m_blog.dto.user.UserDetailDTO;
import com.ming.m_blog.enums.CommentTypeEnum;
import com.ming.m_blog.exception.ReRuntimeException;
import com.ming.m_blog.mapper.*;
import com.ming.m_blog.pojo.Comment;
import com.ming.m_blog.pojo.UserAuth;
import com.ming.m_blog.pojo.UserInfo;
import com.ming.m_blog.service.BlogService;
import com.ming.m_blog.service.CommentService;
import com.ming.m_blog.service.RedisService;
import com.ming.m_blog.utils.*;
import com.ming.m_blog.vo.AdminCommentsVO;
import com.ming.m_blog.vo.CommentsVO;
import com.ming.m_blog.vo.PageResult;
import com.ming.m_blog.vo.WebsiteConfigVO;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.ming.m_blog.enums.CommentTypeEnum.*;
import static com.ming.m_blog.constant.CommonConst.*;
import static com.ming.m_blog.constant.RedisPrefixConst.COMMENT_LIKE_COUNT;
import static com.ming.m_blog.constant.RedisPrefixConst.COMMENT_USER_LIKE;

/**
 * 评论服务
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

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private TalkMapper talkMapper;

    @Value("${website.url}")
    private String websiteUrl;

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
        WebsiteConfigVO websiteConfig = blogService.getWebsiteConfig();
        Integer isReview = websiteConfig.getIsCommentReview();
        // 获取登录用户
        UserDetailDTO loginUser = UserUtils.getLoginUser();
        // 过滤标签并且过滤敏感词
        String filterComment = HTMLUtils.filter(commentsVO.getCommentContent());
        // 如果不是友链评论的话，过滤敏感词
        if (!commentsVO.getType().equals(FRIEND_LINK.getType())){
            filterComment = HTMLUtils.sensitiveFilter(filterComment);
        }
        commentsVO.setCommentContent(filterComment);

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
        // 查看是否开启邮件通知功能
        if (websiteConfig.getIsEmailNotice() == TRUE){
            CompletableFuture.runAsync(() -> noticeEmail(comment, loginUser));
        }

        return commentMapper.insert(comment);
    }

    // 查询主题下的评论列表
    @Override
    public PageResult<CommentDTO> getComments(CommentsVO commentsVO) {
        // 查询改主题下有多少评论
        Integer commentCount = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(ObjectUtil.isNotEmpty(commentsVO.getTopicId()), Comment::getTopicId, commentsVO.getTopicId())
                .eq(Comment::getType, commentsVO.getType())
                .isNull(Comment::getParentId)
                .eq(Comment::getIsReview, TRUE)
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

    /**
     * 发送评论邮件通知
     * @param comment 评论信息
     */
    public void noticeEmail(Comment comment, UserDetailDTO loginUser) {
        // 默认是自己的id
        Integer userAuthId = BLOG_ID;
        Integer userInfoId = BLOG_ID;

        // 获取回复用户的userInfo Id  这里因为之前没有统一好存userInfo还是UserAuth导致有点麻烦
        // 判断是否有回复用户，如果没有说明可能是文章或者说说
        if (Objects.nonNull(comment.getReplyUserId())){
            // 说明这里有回复的用户，不一定是博主
            userAuthId = comment.getReplyUserId();
            userInfoId = userAuthMapper.selectById(userAuthId).getUserInfoId();
        }else {
            // 当前没有回复用户，可能是说说或者博客
            switch (Objects.requireNonNull(getCommentTypeEnum(comment.getType()))){
                case ARTICLE:
                    userAuthId = articleMapper.selectById(comment.getTopicId()).getUserId();
                    userInfoId = userAuthMapper.selectById(userAuthId).getUserInfoId();
                    break;
                case TALK:
                    userInfoId = talkMapper.selectById(comment.getTopicId()).getUserId();
            }
        }

        // 获取回复的邮箱
        UserInfo userInfo = userInfoMapper.selectById(userInfoId);
        String email = userInfo.getEmail();
        if (StringUtils.isBlank(email)){
            return;
        }
        // 校验格式
        if (!CommonUtils.checkEmail(email)){
            throw new ReRuntimeException("邮箱格式错误");
        }
        // 发送邮件

        // 发送给被评论用户 还是博主
        EmailSendDTO emailSendDTO = new EmailSendDTO();
        // 如果已经审核了，直接发送给被回复的用户
        if (comment.getIsReview().equals(TRUE)){
            System.out.println(getCommentPath(comment.getType()));
            String url = this.websiteUrl + getCommentPath(comment.getType()) + comment.getTopicId();
            // 发送回复用户
            String content = String.format("你好，%s，%s回复了你的评论。请前往%s 页面查看",
                    userInfo.getNickname(), loginUser.getNickname(),url);
            emailSendDTO = EmailSendDTO.builder()
                    .email(email)
                    // TODO 将 博客 换成主题的详细信息
                    .subject("你在博客的评论有了新回复")
                    .content(content)
                    .build();
        }else {
            // 如果没有审核，将邮件发送给博主
            String blogEmail = userInfoMapper.selectById(BLOG_ID).getEmail();
            emailSendDTO.setEmail(blogEmail);
            emailSendDTO.setSubject("审核提醒");
            emailSendDTO.setContent("有一条新的评论，请去博客后台审核");
        }
        // 直接使用对象发送,向消费者发送消息
        rabbitTemplate.convertAndSend(MQPrefixConst.EMAIL_EXCHANGE,"",emailSendDTO, message -> {
            // 设置消息唯一id
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setMessageId(UUIDUtils.getUUID());
            return message;
        });
    }

    /**
     * 获取评论路径
     */
    private String getCommentPath(Integer type){
        for (CommentTypeEnum value : CommentTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value.getPath();
            }
        }
        return null;
    }

}
