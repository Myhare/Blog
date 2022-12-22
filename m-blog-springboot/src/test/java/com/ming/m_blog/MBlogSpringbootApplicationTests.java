package com.ming.m_blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class MBlogSpringbootApplicationTests {

    @Test
    void contextLoads() {

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


}
