package com.ming.m_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.m_blog.dto.message.MessageBackDTO;
import com.ming.m_blog.dto.message.MessageDTO;
import com.ming.m_blog.pojo.Message;
import com.ming.m_blog.service.BlogService;
import com.ming.m_blog.service.MessageService;
import com.ming.m_blog.mapper.MessageMapper;
import com.ming.m_blog.utils.BeanCopyUtils;
import com.ming.m_blog.utils.HTMLUtils;
import com.ming.m_blog.utils.IpUtils;
import com.ming.m_blog.utils.PageUtils;
import com.ming.m_blog.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ming.m_blog.constant.CommonConst.*;

/**
 * @author 86135
 * @description 针对表【message】的数据库操作Service实现
 * @createDate 2023-02-09 10:07:05
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
        implements MessageService{

    @Autowired
    private BlogService blogService;
    @Autowired
    private MessageMapper messageMapper;
    @Resource
    private HttpServletRequest request;

    // 添加留言
    @Override
    public void saveMessage(MessageVO messageVO) {
        // 判断留言是否需要审核
        Integer isReview = blogService.getWebsiteConfig().getIsMessageReview();
        // 获取用户IP地址
        String ipAddress = IpUtils.getIpAddress(request);
        String ipSource = IpUtils.getIpSource(ipAddress);
        Message message = BeanCopyUtils.copyObject(messageVO, Message.class);
        message.setMessageContent(HTMLUtils.filter(message.getMessageContent()));
        message.setIpAddress(ipAddress);
        message.setIsReview(isReview == TRUE ? FALSE : TRUE);
        message.setIpSource(ipSource);
        messageMapper.insert(message);
    }

    // 查询留言列表
    @Override
    public List<MessageDTO> listMessage() {
        List<Message> messageList = messageMapper.selectList(new LambdaQueryWrapper<Message>()
                .select(Message::getId, Message::getNickname, Message::getAvatar, Message::getMessageContent, Message::getTime)
                .eq(Message::getIsReview, TRUE)
        );
        return BeanCopyUtils.copyList(messageList, MessageDTO.class);
    }

    // 后台分页查询留言列表
    @Override
    public PageResult<MessageBackDTO> adminListMessage(QueryInfoVO queryInfoVO) {
        // 分页查询留言列表
        Page<Message> page = new Page<>(PageUtils.getLimitCurrent(),PageUtils.getSize());
        Page<Message> messagePage = messageMapper.selectPage(page, new LambdaQueryWrapper<Message>()
                .like(StringUtils.isNotBlank(queryInfoVO.getKeywords()), Message::getNickname, queryInfoVO.getKeywords())
                .eq(Objects.nonNull(queryInfoVO.getIsReview()),Message::getIsReview, queryInfoVO.getIsReview())
                .orderByDesc(Message::getId)
        );
        List<MessageBackDTO> messageBackDTOS = BeanCopyUtils.copyList(messagePage.getRecords(), MessageBackDTO.class);
        return new PageResult<>(BeanCopyUtils.copyList(messagePage.getRecords(), MessageBackDTO.class),(int)messagePage.getTotal());
    }

    // 修改留言审核状态
    @Override
    @Transactional
    public void updateMessageReview(ReviewVO reviewVO) {
        List<Message> messageList = reviewVO.getIdList().stream()
                .map(id -> {
                    return Message.builder()
                            .id(id)
                            .isReview(reviewVO.getIsReview())
                            .build();
                }).collect(Collectors.toList());
        this.updateBatchById(messageList);
    }
}




