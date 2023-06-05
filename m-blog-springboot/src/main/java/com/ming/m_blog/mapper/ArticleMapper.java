package com.ming.m_blog.mapper;

import com.ming.m_blog.dto.article.*;
import com.ming.m_blog.pojo.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ming.m_blog.vo.AdminArticlesVO;
import com.ming.m_blog.vo.ArticleListConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Ming
 * @since 2022-08-11
 */
@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 根据条件查询博客的数量
     * @param adminArticlesVO 查询条件
     * @return                查询到的数量
     */
    Integer getArticleCount(@Param("adminArticlesVO") AdminArticlesVO adminArticlesVO);

    /**
     * 根据条件查询文章列表
     * @param adminArticlesVO 查询条件
     * @return                查询结果
     */
    List<ArticleListInfoDTO> getArticleList(@Param("adminArticlesVO") AdminArticlesVO adminArticlesVO);

    /**
     * 查询
     * @param pageNum       从哪一页开始查询
     * @param pageSize      查询多少数量getArticleById
     * @return              查询结果
     */
    List<HomeArticleDTO> getHomeLArticle(@Param("pageNum") Long pageNum, @Param("pageSize") Long pageSize);

    /**
     * 登录之后获取前台文章列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<HomeArticleDTO> loginGetHomeArticle(@Param("pageNum") Long pageNum, @Param("pageSize") Long pageSize, @Param("userId")Integer userId);

    /**
     * 查询文章预览列表
     * @param conditionVO   查询条件
     * @return              查询结果
     */
    List<ArticlePreviewDTO> getArticleCondition(@Param("conditionVO") ArticleListConditionVO conditionVO,
                                                @Param("pageNum")Long pageNum,
                                                @Param("pageSize")Long pageSize);

    /**
     * 登录后查询文章预览列表
     * @param conditionVO 查询条件
     * @param pageNum      分页号
     * @param pageSize     一页多少个
     * @param userId       登录用户id
     * @return             查询结果
     */
    List<ArticlePreviewDTO> loginGetArticleCondition(@Param("conditionVO") ArticleListConditionVO conditionVO,
                                                     @Param("pageNum")Long pageNum,
                                                     @Param("pageSize")Long pageSize,
                                                     @Param("userId") Integer userId
    );
    /**
     * 查询推荐文章
     * @param articleId 文章id
     * @return           查询结果
     */
    List<ArticleRecommendDTO> getArticleRecommend(Integer articleId);

    /**
     * 通过id查询文章详细信息
     * @param articleId 文章id
     * @return           查询结果
     */
    ArticleDTO getArticleDetailedById(Integer articleId);

    /**
     * 查询文章统计列表
     */
    List<ArticleStatisticsDTO> listArticleStatistics();

    /**
     * 登录用户查询归档
     * @param userId 登录用户id
     */
    List<ArchiveDTO> loginGetArchive(@Param("pageNum")Long pageNum,
                                     @Param("pageSize")Long pageSize,
                                     @Param("userId") Integer userId
    );

    /**
     * 登录后查询归档文章数量
     * @param userId    登录用户id
     * @return
     */
    Integer loginGetArchiveCount(@Param("userId")Integer userId);

    /**
     * 批量彻底删除文章列表
     * @param articleIdList 文章id列表
     * @return              影响行数
     */
    Integer reallyDelArticleList(@Param("articleIdList")List articleIdList);

    /**
     * 批量恢复已删除的文章
     * @param articleIdList 文章列表
     * @return              影响的行数
     */
    Integer restoreArticle(@Param("articleIdList")List articleIdList);

}
