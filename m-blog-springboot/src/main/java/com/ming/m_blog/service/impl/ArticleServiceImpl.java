package com.ming.m_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ming.m_blog.dto.*;
import com.ming.m_blog.enums.ArticleStatusEnum;
import com.ming.m_blog.exception.ReRuntimeException;
import com.ming.m_blog.mapper.CategoryMapper;
import com.ming.m_blog.mapper.InArticleTagMapper;
import com.ming.m_blog.mapper.TagMapper;
import com.ming.m_blog.pojo.Article;
import com.ming.m_blog.mapper.ArticleMapper;
import com.ming.m_blog.pojo.Category;
import com.ming.m_blog.pojo.InArticleTag;
import com.ming.m_blog.pojo.Tag;
import com.ming.m_blog.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.m_blog.utils.BeanCopyUtils;
import com.ming.m_blog.utils.CommonUtils;
import com.ming.m_blog.utils.PageUtils;
import com.ming.m_blog.utils.UserUtils;
import com.ming.m_blog.vo.*;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.ming.m_blog.constant.CommonConst.*;
import static com.ming.m_blog.constant.RedisPrefixConst.*;
import static com.ming.m_blog.enums.ArticleStatusEnum.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ming
 * @since 2022-08-11
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private InArticleTagMapper inArticleTagMapper;
    @Autowired
    private RedisServiceImpl redisService;
    @Autowired
    private HttpSession session;

    // 添加一个文章
    @Override
    @Transactional
    public boolean addArticle(ArticleAddVO articleAddVO) {

        // 获取文章分类名称
        String categoryName = articleAddVO.getCategoryName();
        // 通过分类名称获取分类id，如果没有当前分类则添加分类
        Category category = null;   // 如果为空说明在保存草稿
        if (categoryName==null){
            category = Category
                    .builder()
                    .id(null)
                    .build();
        }else {
            category = getCategoryByCateName(categoryName);
        }

        UserDetailDTO loginUser = UserUtils.getLoginUser();
        Article article = Article.builder()
                .id(articleAddVO.getId())
                .userId(loginUser.getUserId())
                // .userId(1)
                .categoryId(category.getId())
                .title(articleAddVO.getArticleTitle())
                .content(articleAddVO.getArticleContent())
                .cover(articleAddVO.getCoverUrl())
                .type(articleAddVO.getArticleType())
                .isTop(articleAddVO.getIsTop())
                .status(articleAddVO.getStatus())
                .build();

        // 调用service中的save方法，执行后对象中id会自动添加
        boolean saveFlag = this.saveOrUpdate(article);
        // 添加文章标签
        saveTag(articleAddVO.getTagList(),article.getId());
        return saveFlag;
    }

    // 后台分页查询文章列表
    @Override
    public PageResult<ArticleListInfoDTO> getArticleList(AdminArticlesVO adminArticlesVO) {
        // 将页面大小调整
        Integer pageNum = adminArticlesVO.getPageNum();
        Integer pageSize = adminArticlesVO.getPageSize();
        adminArticlesVO.setPageNum((pageNum-1)*pageSize);
        Integer articleCount = articleMapper.getArticleCount(adminArticlesVO);
        if (articleCount==0){
            return new PageResult<>();
        }
        //  通过分页信息查询文章列表
        List<ArticleListInfoDTO> articleListInfoDTOList = articleMapper.getArticleList(adminArticlesVO);
        // 初始化浏览量和点赞量
        articleListInfoDTOList.forEach(item -> {
            item.setPageViews(0);
            item.setPageLikes(0);
        });
        return new PageResult<ArticleListInfoDTO>(articleListInfoDTOList,articleCount);
    }

    /**
     * 通过文章分类获取文章id，如果没有当前分类创建一个新的分类返回id
     * @param categoryName  分类名称
     * @return              分类信息
     */
    private Category getCategoryByCateName(String categoryName){
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .eq(Category::getCategoryName, categoryName)
        );
        if (category == null){
            category = Category.builder()
                    .categoryName(categoryName)
                    .build();
            // 添加分类
            categoryMapper.insert(category);
        }
        return category;
    }

    /**
     * 添加文章标签
     * @param tagNameList   标签列表
     * @param articleId     文章id
     */
    private void saveTag(List<String> tagNameList,Integer articleId){

        // 添加标签
        for (String tagName : tagNameList) {
            Tag tag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                    .eq(Tag::getTagName, tagName));
            // 如果不存在标签就创建一个新的标签
            if (tag == null){
                tag = Tag.builder()
                        .tagName(tagName)
                        .build();
                tagMapper.insert(tag);
            }
            // 添加中间表数据
            inArticleTagMapper.insert(InArticleTag
                    .builder()
                    .articleId(articleId)
                    .tagId(tag.getId())
                    .build()
            );
        }
    }

    // 删除文章
    @Override
    public int deleteArticle(List<Integer> articleIdList) {
        return articleMapper.deleteBatchIds(articleIdList);
    }

    // 修改文章置顶情况
    @Override
    public int changeArticleTop(ChangeArticleTopVO changeArticleTopVO) {
        Article article = Article.builder()
                .id(changeArticleTopVO.getArticleId())
                .isTop(changeArticleTopVO.getIsTop())
                .build();
        return articleMapper.updateById(article);
    }

    // 通过id查询文章
    @Override
    public ArticleAddVO getArticleById(Integer articleId) {
        Article article = articleMapper.selectById(articleId);
        // 获取文章的分类
        Category category = categoryMapper.selectById(article.getCategoryId());
        String categoryName = null;
        if (!Objects.isNull(category)){
            categoryName = category.getCategoryName();
        }
        // 通过id查询标签
        List<String> tagNameList = tagMapper.getTagNameByArticleId(articleId);
        return ArticleAddVO.builder()
                .id(article.getId())
                .articleTitle(article.getTitle())
                .articleContent(article.getContent())
                .categoryName(categoryName)
                .tagList(tagNameList)
                .articleType(article.getType())
                .coverUrl(article.getCover())
                .isTop(article.getIsTop())
                .status(article.getStatus())
                .build();
    }

    // 获取首页文章
    @Override
    public List<HomeArticleDTO> getHomeArticles() {
        // 判断当前用户是否登录
        try {
            Integer loginUserId = UserUtils.getLoginUserId();
            return articleMapper.loginGetHomeArticle(PageUtils.getLimitCurrent(), PageUtils.getSize(), loginUserId);
        } catch (Exception e) {
            // 用户没有登录,不展示隐藏内容
            return articleMapper.getHomeLArticle(PageUtils.getLimitCurrent(), PageUtils.getSize());
        }
    }

    // 查询文章预览列表
    @Override
    public ArticlePreviewListDTO conditionArticle(ArticleListConditionVO conditionVO) {
        // 搜索对应的条件名称
        String name;
        if (Objects.isNull(conditionVO.getCategoryId())){
            // 搜索标签名称
            name = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                    .eq(Tag::getId,conditionVO.getTagId())
                    .last("limit 1")
            ).getTagName();
        }else {
            // 搜索条件名称
            name = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                    .eq(Category::getId, conditionVO.getCategoryId())
                    .last("limit 1")
            ).getCategoryName();
        }
        // 查询数据
        try {
            Integer loginUserId = UserUtils.getLoginUserId();
            List<ArticlePreviewDTO> articlePreviewDTOList = articleMapper.loginGetArticleCondition(conditionVO, PageUtils.getLimitCurrent(), PageUtils.getSize(), loginUserId);
            return ArticlePreviewListDTO.builder()
                    .name(name)
                    .articlePreviewDTOList(articlePreviewDTOList)
                    .build();
        } catch (Exception e) {
            List<ArticlePreviewDTO> articlePreviewDTOList = articleMapper.getArticleCondition(conditionVO, PageUtils.getLimitCurrent(), PageUtils.getSize());
            return ArticlePreviewListDTO.builder()
                    .name(name)
                    .articlePreviewDTOList(articlePreviewDTOList)
                    .build();
        }
    }

    // 查询主页详细文章
    @Override
    public ArticleDTO getArticle(Integer articleId) {
        // 判断用户是否有权限访问
        articleAuthentication(articleId);
        // 更新文章阅读量
        updateArticleViewCount(articleId);
        // 查询当前文章详细信息
        ArticleDTO articleDTO = articleMapper.getArticleDetailedById(articleId);
        // 查询推荐文章
        CompletableFuture<List<ArticleRecommendDTO>> articleRecommend =
                CompletableFuture.supplyAsync(() -> articleMapper.getArticleRecommend(articleId));
        // 查询最新文章
        CompletableFuture<List<ArticleRecommendDTO>> newestArticleList = CompletableFuture.supplyAsync(() -> {
            List<Article> articleNewestList = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                    .select(Article::getId, Article::getCover, Article::getTitle, Article::getUpdateTime)
                    .eq(Article::getStatus, PUBLIC.getStatus())
                    .orderByDesc(Article::getUpdateTime)
                    .last("limit 5")
            );
            return BeanCopyUtils.copyList(articleNewestList, ArticleRecommendDTO.class);
        });
        // 查询上一篇文章
        Article lastArticle = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getCover, Article::getTitle)
                .eq(Article::getStatus, ArticleStatusEnum.PUBLIC.getStatus())
                .lt(Article::getId, articleId)
                .orderByDesc(Article::getId)
                .last("limit 1")
        );
        ArticlePaginationDTO lastArticleDTO = BeanCopyUtils.copyObject(lastArticle, ArticlePaginationDTO.class);
        // 查询下一篇文章
        Article nextArticle = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getCover, Article::getTitle)
                .eq(Article::getStatus, ArticleStatusEnum.PUBLIC.getStatus())
                .gt(Article::getId, articleId)
                .orderByDesc(Article::getId)
                .last("limit 1")
        );
        ArticlePaginationDTO nextArticleDTO = BeanCopyUtils.copyObject(nextArticle, ArticlePaginationDTO.class);
        // 查询阅读数和点赞数
        Double viewCount = redisService.zScore(ARTICLE_VIEW_COUNT, articleId);
        Object likeCount = redisService.hGet(ARTICLE_LIKE_COUNT, articleId.toString());
        articleDTO.setViewsCount(viewCount.intValue());
        articleDTO.setLikeCount(Optional.ofNullable(likeCount).orElse(0).toString());
        // 组合数据
        try {
            articleDTO.setRecommendArticleList(articleRecommend.get());  // 推荐文章
            articleDTO.setNewestArticleList(newestArticleList.get());    // 最新文章
        } catch (Exception e) {
            e.printStackTrace();
        }
        articleDTO.setLastArticle(lastArticleDTO);
        articleDTO.setNextArticle(nextArticleDTO);
        return articleDTO;
    }

    // 查询文章归档列表
    @Override
    public PageResult<ArchiveDTO> getArchive(Integer current) {
        try {
            // 登录后可以查询当前用的发表的私密文章
            Integer loginUserId = UserUtils.getLoginUserId();
            List<ArchiveDTO> archiveDTOList = articleMapper.loginGetArchive(PageUtils.getLimitCurrent(), PageUtils.getSize(), loginUserId);
            Integer archiveCount = articleMapper.loginGetArchiveCount(loginUserId);
            return new PageResult<ArchiveDTO>(archiveDTOList,archiveCount);
        } catch (Exception e) {
            // 用户没有登录
            Page<Article> articlePage =
                    articleMapper.selectPage(new Page<>(PageUtils.getLimitCurrent(), PageUtils.getSize()),
                            new LambdaQueryWrapper<Article>()
                                    .select(Article::getId, Article::getTitle, Article::getCreateTime)
                                    .eq(Article::getStatus, ArticleStatusEnum.PUBLIC.getStatus())
                    );
            List<ArchiveDTO> archiveDTOList = BeanCopyUtils.copyList(articlePage.getRecords(), ArchiveDTO.class);
            // 查询一共发布了多少文章
            Integer archiveCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                    .eq(Article::getStatus, ArticleStatusEnum.PUBLIC.getStatus())
            );
            return new PageResult<ArchiveDTO>(archiveDTOList,archiveCount);
        }
    }

    /**
     * 判断文章是否可以访问
     * @param articleId 文章id
     */
    private void articleAuthentication(Integer articleId){
        // 判断文章是不是私有，如果是，只有文章发布者可以查看
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getStatus, Article::getUserId)
                .eq(Article::getId, articleId)
        );
        if (Objects.isNull(article)){
            throw new ReRuntimeException("文章不存在");
        }
        Integer loginUserId;
        switch (Objects.requireNonNull(getArticleStatus(article.getStatus()))){
            case DRAFT:
                // 草稿箱
                throw new ReRuntimeException("文章非法");
            case SECRET:
                // 私密文章，检查用户是否登录成功
                try {
                    loginUserId = UserUtils.getLoginUserId();
                } catch (Exception e) {
                    throw new ReRuntimeException("请先登录再获取文章");
                }
                if (!article.getUserId().equals(loginUserId)){
                    throw new ReRuntimeException("你没有权限访问此文章");
                }
            default:
                break;
        }
    }

    /**
     * 更新文章阅读量
     */
    private void updateArticleViewCount(Integer articleId){
        // 判断是不是第一次阅读，是的话添加一次阅读量
        Set<Integer> articleSet = CommonUtils.castSet(Optional.ofNullable(session.getAttribute(ARTICLE_SET)).orElseGet(HashSet::new), Integer.class);
        // 如果不包含指定元素，浏览量加一
        if (!articleSet.contains(articleId)){
            articleSet.add(articleId);
            session.setAttribute(ARTICLE_SET,articleSet);
            redisService.zIncr(ARTICLE_VIEW_COUNT,articleId, 1.0);
        }
    }

}
