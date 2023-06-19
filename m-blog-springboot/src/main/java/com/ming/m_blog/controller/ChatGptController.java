package com.ming.m_blog.controller;

import com.ming.chatgptdemo.client.ChatClient;
import com.ming.m_blog.constant.CommonConst;
import com.ming.m_blog.exception.ReRuntimeException;
import com.ming.m_blog.service.BlogService;
import com.ming.m_blog.vo.WebsiteConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;

/**
 * chatGPT聊天功能
 */
@RestController
public class ChatGptController {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private BlogService blogService;

    @GetMapping("/sendStream/{message}")
    public SseEmitter send(@PathVariable("message") String message, HttpServletResponse response){
        WebsiteConfigVO websiteConfig = blogService.getWebsiteConfig();
        if (websiteConfig.getIsChatGpt() == CommonConst.FALSE){
            throw new ReRuntimeException("管理员未开启人工智能");
        }
        // 修改请求头类型为持久化响应
        // response.setHeader("Connection", "keep-alive");
        SseEmitter sseEmitter = new SseEmitter();
        try {
            chatClient.streamSend(sseEmitter, message, streamReDTO -> {
                // TODO 通过完整的回复进行一些操作，比如存数据库之类的
                System.out.println("完整的回复是："+  streamReDTO.getContent());
            });
        } catch (Exception e) {
            sseEmitter.completeWithError(e);
        }
        return sseEmitter;
    }

}
