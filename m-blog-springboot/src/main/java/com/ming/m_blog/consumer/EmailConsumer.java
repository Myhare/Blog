package com.ming.m_blog.consumer;

import com.alibaba.fastjson.JSON;
import com.ming.m_blog.constant.MQPrefixConst;
import com.ming.m_blog.dto.EmailSendDTO;
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

    @RabbitHandler
    public void sendEmail(EmailSendDTO emailSendDTO){
        // 发送邮件
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailProperties.getUsername());
        mailMessage.setTo(emailSendDTO.getEmail());
        mailMessage.setSubject(emailSendDTO.getSubject());
        mailMessage.setText(emailSendDTO.getContent());
        mailSender.send(mailMessage);
    }

}
