package com.ming.m_blog;

import com.ming.m_blog.dto.CategoryDTO;
import com.ming.m_blog.service.CategoryService;
import com.ming.m_blog.vo.PageResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CategoryTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    void addCategory(){
        String categoryName = "测试分类";
        int i = categoryService.addCateGory(categoryName);
        if (i>0){
            System.out.println("添加分类成功");
        }else {
            System.out.println("添加分类失败");
        }
    }

    @Test
    void listCategory(){
        PageResult<CategoryDTO> categoryDTOPageResult = categoryService.listCategory();
        categoryDTOPageResult.getRecordList().forEach(System.out::println);
    }


}
