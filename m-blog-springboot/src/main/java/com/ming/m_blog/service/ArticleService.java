package com.ming.m_blog.service;

import com.ming.m_blog.dto.article.*;
import com.ming.m_blog.pojo.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.m_blog.vo.*;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Ming
 * @since 2022-08-11
 */
public interface ArticleService extends IService<Article> {

    /**
     * 查询主页信息
     * @param articleId     文章id
     * @return              文章页面详细系信息
     */
    ArticleDTO getArticle(Integer articleId);

    /**
     * 添加一个文章
     * @param articleAddVO 文章对象
     * @return             是否添加成功
     */
    boolean addOrUpdateArticle(ArticleAddVO articleAddVO);

    /**
     * 后台分页查询文章列表
     * @param adminArticlesVO 查询数据对象
     * @return                获取到的结果
     */
    PageResult<ArticleListInfoDTO> getArticleList(AdminArticlesVO adminArticlesVO);

    /**
     * 删除文章
     * @param articleIdList   要删除的文章id列表
     * @return              影响的行数
     */
    int deleteArticle(List<Integer> articleIdList);

    /**
     * 彻底删除博客
     * @param articleIdList 文章id列表
     * @return              影响行数
     */
    int realDeleteArticle(List<Integer> articleIdList);

    /**
     * 修改用户文章置顶情况
     * @param changeArticleTopVO 要修改的文章信息
     * @return                   影响的行数
     */
    int changeArticleTop(ChangeArticleTopVO changeArticleTopVO);

    /**
     * 通过文章id查询文章信息
     * @param articleId 文章id
     * @return          查询结果
     */
    ArticleAddVO getArticleById(Integer articleId);

    /**
     * 获取主页展示文章
     * @return
     */
    List<HomeArticleDTO> getHomeArticles();

    /**
     * 查询文章预览列表
     * @param conditionVO   查询条件
     * @return              查询结果
     */
    ArticlePreviewListDTO conditionArticle(ArticleListConditionVO conditionVO);

    /**
     * 查询文章归档列表
     * @param current 查询次数
     * @return        查询结果
     */
    PageResult<ArchiveDTO> getArchive(Integer current);

    /**
     * 批量恢复删除了的博客
     * @param articleIdList 博客id列表
     * @return              影响行数
     */
    Integer restoreArticle(List<Integer> articleIdList);

}
