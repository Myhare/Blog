package com.ming.m_blog;

import com.ming.m_blog.dto.BlogInfoDTO;
import com.ming.m_blog.service.BlogService;
import com.ming.m_blog.utils.IpUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BlogTest {

    @Autowired
    private BlogService blogService;

    // 查询博客基本信息
    @Test
    public void selectBlogInfo(){
        BlogInfoDTO blogInfo = blogService.getBlogInfo();
        System.out.println(blogInfo);
    }

    // 测试ip地址
    @Test
    public void ipTest(){
        String ipSource = IpUtils.getIpSource("192.168.1.105");
        System.out.println(ipSource);
    }

}
