package com.ming.m_blog.consumer;

import com.ming.m_blog.constant.MQPrefixConst;
import com.ming.m_blog.dto.EmailSendDTO;
import com.ming.m_blog.service.RedisService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@RabbitListener(queues = MQPrefixConst.EMAIL_QUEUE)
public class EmailConsumer {

    @Resource
    private MailProperties mailProperties;

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private RedisService redisService;

    @RabbitHandler
    public void sendEmail(EmailSendDTO emailSendDTO, Message message){
        String messageId = message.getMessageProperties().getMessageId();
        // 消息一致性
        if (redisService.hasKey(messageId)) {
            return;
        }
        // 发送邮件
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailProperties.getUsername());
        mailMessage.setTo(emailSendDTO.getEmail());
        mailMessage.setSubject(emailSendDTO.getSubject());
        mailMessage.setText(emailSendDTO.getContent());
        mailSender.send(mailMessage);
        // 发送完之后将消息存入redis, 设置十分钟有效期
        redisService.set(messageId, "1", 60 * 10);
    }

}
