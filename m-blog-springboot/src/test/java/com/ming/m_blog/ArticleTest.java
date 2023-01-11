package com.ming.m_blog;

import com.ming.m_blog.dto.*;
import com.ming.m_blog.service.ArticleService;
import com.ming.m_blog.vo.AdminArticlesVO;
import com.ming.m_blog.vo.ArticleAddVO;
import com.ming.m_blog.vo.ArticleListConditionVO;
import com.ming.m_blog.vo.PageResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ArticleTest {

    @Autowired
    private ArticleService articleService;

    // 测试添加博客
    @Test
    void addArticle(){
        List<String> tagList = new ArrayList<>();
        tagList.add("111");
        tagList.add("222");
        tagList.add("333");
        ArticleAddVO articleAddVO = ArticleAddVO.builder()
                .id(null)
                .articleTitle("测试博客")
                .articleContent("这是测试博客")
                .categoryName("测试添加博客分类名称")
                .tagList(tagList)
                .articleType(1)
                .coverUrl("http://www.mingzib.xyz:81/blogGetDefaultFile/deafaultArticleCover.png")
                .isTop(0)
                .status(1)
                .build();
        boolean flag = articleService.addOrUpdateArticle(articleAddVO);
        if (flag){
            System.out.println("添加博客成功");
        }else {
            System.out.println("添加博客失败");
        }
    }

    // 测试分页查询博客
    @Test
    void selectArticleList(){
        AdminArticlesVO adminArticlesVO = AdminArticlesVO.builder()
                .status(null)
                .categoryId(null)
                .tagId(null)
                .pageNum(0)
                .pageSize(2)
                .keywords("")
                .build();
        PageResult<ArticleListInfoDTO> pageResult = articleService.getArticleList(adminArticlesVO);
        List<ArticleListInfoDTO> articleList = pageResult.getRecordList();
        Integer count = pageResult.getCount();
        System.out.println("共查询到了"+count+"条数据");
        articleList.forEach(System.out::println);
    }

    // 通过id查询文章
    @Test
    void selectArticleById(){
        ArticleAddVO article = articleService.getArticleById(4);
        System.out.println(article);
    }

    // 查询首页文章
    @Test
    void selectHomeArticle(){
        List<HomeArticleDTO> homeArticles = articleService.getHomeArticles();
        homeArticles.forEach(System.out::println);
    }

    // 查询文件预览列表
    @Test
    void selectArticleCondition(){
        ArticleListConditionVO conditionVO = new ArticleListConditionVO();
        conditionVO.setTagId(1);
        ArticlePreviewListDTO articlePreviewListDTO = articleService.conditionArticle(conditionVO);
        System.out.println(articlePreviewListDTO);

    }

    // 查询文章详细信息
    @Test
    void selectArticleDetail(){
        ArticleDTO article = articleService.getArticle(1);
        System.out.println(article);
    }

    // 查询文章归档
    @Test
    void getArchive(){
        PageResult<ArchiveDTO> pageResult = articleService.getArchive(1);
        System.out.println(pageResult);
    }

}
