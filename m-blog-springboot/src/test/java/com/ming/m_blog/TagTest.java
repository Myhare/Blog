package com.ming.m_blog;

import com.ming.m_blog.dto.TagListDTO;
import com.ming.m_blog.dto.TagSimpleDTO;
import com.ming.m_blog.service.TagService;
import com.ming.m_blog.vo.PageResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TagTest {

    @Autowired
    private TagService tagService;

    @Test
    void getTagList(){
        List<TagListDTO> tagList = tagService.getTagList(1, 2, "");
        tagList.forEach(System.out::println);
    }

    @Test
    void tags(){
        PageResult<TagSimpleDTO> tags = tagService.getTags();
        System.out.println(tags);
    }

}
