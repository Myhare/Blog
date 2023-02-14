package com.ming.m_blog.controller;

import com.ming.m_blog.annotation.AccessLimit;
import com.ming.m_blog.annotation.OptLog;
import com.ming.m_blog.constant.OptTypeConstant;
import com.ming.m_blog.dto.MessageBackDTO;
import com.ming.m_blog.dto.MessageDTO;
import com.ming.m_blog.service.MessageService;
import com.ming.m_blog.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 留言处理器
 */
@Api(tags = "留言处理")
@RestController
public class MessageController {


    @Autowired
    private MessageService messageService;


    /**
     * 添加留言
     * @param messageVO 留言信息
     */
    @AccessLimit(seconds = 60, maxCount = 1)
    @ApiOperation(value = "添加留言")
    @PostMapping("/messages")
    public ResponseResult<?> saveMessage(@Valid @RequestBody MessageVO messageVO) {
        messageService.saveMessage(messageVO);
        return ResponseResult.ok();
    }

    @ApiOperation(value = "查看留言列表")
    @GetMapping("/messages")
    public ResponseResult<List<MessageDTO>> listMessage(){
        return ResponseResult.ok(messageService.listMessage());
    }

    @ApiOperation(value = "后台查询留言列表")
    @GetMapping("/admin/messages")
    public ResponseResult<PageResult<MessageBackDTO>> getMessageList(QueryInfoVO queryInfoVO){
        return ResponseResult.ok(messageService.adminListMessage(queryInfoVO));
    }

    @OptLog(optType = OptTypeConstant.REMOVE)
    @ApiOperation(value = "后台删除留言")
    @PostMapping("/admin/delMessage")
    public ResponseResult<?> delMessage(@RequestBody List<Integer> messageIdList){
        messageService.removeByIds(messageIdList);
        return ResponseResult.ok();
    }

    @OptLog(optType = OptTypeConstant.UPDATE)
    @ApiOperation(value = "更新留言审核")
    @PostMapping("/admin/updateMessage")
    public ResponseResult<?> updateMessage(@Valid @RequestBody ReviewVO reviewVO){
        messageService.updateMessageReview(reviewVO);
        return ResponseResult.ok();
    }

}
