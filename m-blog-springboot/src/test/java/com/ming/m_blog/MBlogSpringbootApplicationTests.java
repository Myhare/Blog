package com.ming.m_blog;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ming.m_blog.service.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MBlogSpringbootApplicationTests {

    @Resource
    private MailProperties mailProperties;

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private BlogService blogService;

    @Test
    void contextLoads() {
        System.out.println(IdWorker.getId());
    }
    // 测试密码加密
    @Test
    void testBCryptPasswordEncoder() {
        // SpringSecurity底层中使用的是下面的加密方法
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode1 = passwordEncoder.encode("123456");  // 传入密码的原文，返回加密后的字符串
        // String encode2 = passwordEncoder.encode("123456");
        System.out.println(encode1);
        // System.out.println(encode2);   // 每一次自动生成的对象都是不一样的
        // 校验密码
        // 校验密码的时候需要传入加密后的字符串（数据库中查找）,和用户输入的明文密码进行比较，如果一致返回true，否则返回false
        boolean matches = passwordEncoder.matches("123456", "$2a$10$HRmP/ugSgqXaTv7TzvylmuHsgqOytTAugEd7mdG5PHwMI0oJb1jh6");
        System.out.println(matches);
    }

    // 测试发送邮件
    @Test
    void testEmail(){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailProperties.getUsername());  // 寄件人
        mailMessage.setTo("1940307627@qq.com"); // 发件人
        mailMessage.setSubject("邮件主题：嘚嘚");
        mailMessage.setText("邮件文本信息:hello,dede,これは testEmails");
        mailSender.send(mailMessage);
        System.out.println("发送邮件成功");
    }

    // 查看当前网站配置
    @Test
    void testWebsiteConfig(){
        System.out.println(JSON.toJSONString(blogService.getWebsiteConfig()));
    }


}
