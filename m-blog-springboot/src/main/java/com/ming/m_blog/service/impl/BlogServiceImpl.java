package com.ming.m_blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ming.m_blog.dto.article.ArticleRankDTO;
import com.ming.m_blog.dto.article.ArticleStatisticsDTO;
import com.ming.m_blog.dto.blogInfo.BlogBackStatisticsDTO;
import com.ming.m_blog.dto.blogInfo.BlogInfoDTO;
import com.ming.m_blog.dto.blogInfo.UniqueViewDTO;
import com.ming.m_blog.dto.category.CategoryDTO;
import com.ming.m_blog.dto.tag.TagSimpleDTO;
import com.ming.m_blog.dto.user.UserDetailDTO;
import com.ming.m_blog.enums.ArticleStatusEnum;
import com.ming.m_blog.mapper.*;
import com.ming.m_blog.pojo.*;
import com.ming.m_blog.service.BlogService;
import com.ming.m_blog.service.UniqueViewService;
import com.ming.m_blog.strategy.context.SearchStrategyContext;
import com.ming.m_blog.utils.BeanCopyUtils;
import com.ming.m_blog.utils.IpUtils;
import com.ming.m_blog.utils.UserUtils;
import com.ming.m_blog.vo.QueryInfoVO;
import com.ming.m_blog.vo.SearchVO;
import com.ming.m_blog.vo.WebsiteConfigVO;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.ming.m_blog.constant.RedisPrefixConst.*;
import static com.ming.m_blog.constant.CommonConst.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private WebsiteConfigMapper websiteConfigMapper;
    @Autowired
    private RedisServiceImpl redisService;
    @Autowired
    private PageMapper pageMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UniqueViewService uniqueViewService;

    @Resource
    private HttpServletRequest request;

    @Autowired
    private SearchStrategyContext searchStrategyContext;

    // 数据库中website的id
    public static final Integer WEBSITE_CONFIG_ID = 1;

    // 获取博客基本信息
    @Override
    @Transactional(rollbackFor=Exception.class)
    public BlogInfoDTO getBlogInfo() {
        // 查询博客文章数量
        Integer articleCount = 0;
        if (UserUtils.isLogin()){
            UserDetailDTO loginUser = UserUtils.getLoginUser();
            // 用户登录后，可以看到自己发布的博客数量
            articleCount  = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                            .eq(Article::getIsDelete, FALSE)
                    .ne(Article::getStatus, ArticleStatusEnum.DRAFT.getStatus())  // 不能是草稿
                    .ne(Article::getStatus, ArticleStatusEnum.SECRET.getStatus())  // 不是私密
                    .or()
                    .eq(Article::getIsDelete, FALSE)
                    .eq(Article::getUserId,loginUser.getUserId())  // 可以看到自己的私密文章数量
                    .ne(Article::getStatus, ArticleStatusEnum.DRAFT.getStatus()));  // 不能是草稿

        }else {
            // 没有登录，正常获取非私有的博客
            articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                    .eq(Article::getIsDelete, FALSE)
                    .ne(Article::getStatus, ArticleStatusEnum.DRAFT.getStatus())  // 不是草稿
                    .ne(Article::getStatus, ArticleStatusEnum.SECRET.getStatus())  // 不是私密
            );
        }

        // 查询分类数量
        Integer categoryCount = categoryMapper.selectCount(null);
        // 查询标签数量
        Integer tagCount = tagMapper.selectCount(null);

        // 查询封面信息
        List<Page> pageList = pageMapper.selectList(null);

        // 获取网站访问量
        Object blogViewCountObject = redisService.get(BLOG_VIEW_COUNT);
        String blogViewCount = Optional.ofNullable(blogViewCountObject).orElse(0).toString();

        // 获取网站信息
        WebsiteConfigVO websiteConfig = this.getWebsiteConfig();
        return BlogInfoDTO.builder()
                .articleCount(articleCount)
                .categoryCount(categoryCount)
                .tagCount(tagCount)
                .pageList(pageList)
                .websiteConfig(websiteConfig)
                .viewsCount(blogViewCount)
                .build();

    }

    // 获取网站基本信息
    @Override
    public WebsiteConfigVO getWebsiteConfig(){
        WebsiteConfigVO websiteConfigVO;
        // 从redis中获取缓存数据，如果没有就从数据库中获取
        Object websiteConfig = redisService.get(WEBSITE_CONFIG);
        if (Objects.isNull(websiteConfig)){
            // 如果从redis中没查询到，从数据库查找并存入redis中
            String websiteConfigJSON = websiteConfigMapper.selectById(1).getConfig();
            websiteConfigVO = JSON.parseObject(websiteConfigJSON, WebsiteConfigVO.class);
            // 存入redis
            redisService.set(WEBSITE_CONFIG,websiteConfigJSON);
        }else{
            // 直接从redis中获取
            websiteConfigVO = JSON.parseObject(websiteConfig.toString(), WebsiteConfigVO.class);
        }
        return websiteConfigVO;
    }

    // 更新网站基本信息
    @Override
    public void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO) {
        // 将网站基本信息转换成JSON格式存入数据库
        String websiteConfigJSON = JSON.toJSONString(websiteConfigVO);
        websiteConfigMapper.updateById(WebsiteConfig
                .builder()
                .id(1)
                .config(websiteConfigJSON)
                .build()
        );
        // 删除redis中网站基本信息的数据
        redisService.del(WEBSITE_CONFIG);
    }


    // 更新用户访问信息
    @Override
    public void report() {
        String ipAddress = IpUtils.getIpAddress(request);
        // System.out.println("ip地址："+ipAddress);
        // 获取访问设备
        UserAgent userAgent = IpUtils.getUserAgent(request);
        Browser browser = userAgent.getBrowser();
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        String uuid = ipAddress + ":" + browser.getName()+ ":" + operatingSystem.getName();
        String md5 = DigestUtils.md5DigestAsHex(uuid.getBytes(StandardCharsets.UTF_8));

        // 判断用户有没有访问过,如果没有访问过，将信息存入redis
        if (!redisService.sIsMember(UNIQUE_VISITOR,md5)){
            String ipSource = IpUtils.getIpSource(ipAddress);
            // System.out.println("ipSource:"+ipSource);
            // 如果ip信息不为空，将ip地址存入redis中
            if (StringUtils.isNotBlank(ipSource)){
                ipSource = ipSource.substring(0,2)
                        .replaceAll(PROVINCE,"")
                        .replaceAll(CITY,"");
                redisService.hIncr(VISITOR_AREA,ipSource,1L);
            }else {
                redisService.hIncr(VISITOR_AREA,UNKNOWN,1L);
            }
            // 网站访问量加一
            redisService.incr(BLOG_VIEW_COUNT,1L);
            // 保存用户唯一标识
            redisService.sAdd(UNIQUE_VISITOR,md5);
        }
    }

    // 获取后台访问信息DTO
    @Override
    @Transactional(rollbackFor=Exception.class)
    public BlogBackStatisticsDTO getBackStatistics() {
        // 查询访问量
        Object blogViewCountObject = redisService.get(BLOG_VIEW_COUNT);
        Integer blogViewCount = Integer.parseInt(Optional.ofNullable(blogViewCountObject).orElse(0).toString());
        // 查询留言数量
        Integer commentCount = commentMapper.selectCount(null);
        // 查询用户数量
        Integer userCount = userInfoMapper.selectCount(null);
        // 查询文章数量
        Integer articleCount = articleMapper.selectCount(null);
        // 查询分类统计
        List<CategoryDTO> categoryDTOList = categoryMapper.listCategory();
        // 查询标签列表
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId, Tag::getTagName)
        );
        List<TagSimpleDTO> tagSimpleDTOList = BeanCopyUtils.copyList(tagList, TagSimpleDTO.class);
        // 查询文章统计列表
        List<ArticleStatisticsDTO> articleStatisticsDTOList = articleMapper.listArticleStatistics();
        // 查询一周内用户访问量
        List<UniqueViewDTO> uniqueViewDTOAList = uniqueViewService.listUniqueViewDTO();
        // 获取文章浏览量排行
        Map<Object, Double> articleCountMap = redisService.zReverseRangeWithScore(ARTICLE_VIEW_COUNT, 0, 4);
        BlogBackStatisticsDTO blogBackStatisticsDTO = BlogBackStatisticsDTO.builder()
                .viewsCount(blogViewCount)
                .messageCount(commentCount)
                .userCount(userCount)
                .articleCount(articleCount)
                .categoryDTOList(categoryDTOList)
                .tagDTOList(tagSimpleDTOList)
                .articleStatisticsList(articleStatisticsDTOList)
                .uniqueViewDTOList(uniqueViewDTOAList)
                .build();
        // 获取文章浏览量详细信息
        if (!CollectionUtils.isEmpty(articleCountMap)){
            // 查询文章排行
            List<ArticleRankDTO> articleRankDTOList = listArticleRankDTO(articleCountMap);
            blogBackStatisticsDTO.setArticleRankDTOList(articleRankDTOList);
        }
        return blogBackStatisticsDTO;
    }

    // 全局搜索文章
    @Override
    public SearchVO blogSearchList(QueryInfoVO queryInfoVO) {
        return searchStrategyContext.executeSearchArticle(queryInfoVO.getKeywords(),queryInfoVO.getSearchType());
    }



    /**
     * 通过文章id和浏览量查询文章标题
     * @param articleCountMap 文章id的Map
     * @return                加上标题的对象
     */
    private List<ArticleRankDTO> listArticleRankDTO(Map<Object,Double> articleCountMap){
        // 获取文章信息
        ArrayList<Integer> articleIdList = new ArrayList<>();
        articleCountMap.keySet().forEach( articleId ->{
            articleIdList.add((Integer) articleId);
        });
        // 通过文章id列表查询文章标题
        List<Article> articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getTitle)
                .in(Article::getId, articleIdList)
        );
        // 将数据整合后返回
        return articleList.stream()
                .map(article -> ArticleRankDTO
                        .builder()
                        .articleTitle(article.getTitle())
                        .viewsCount(articleCountMap.get(article.getId()).intValue())
                        .build()
                )
                .sorted(Comparator.comparingInt(ArticleRankDTO::getViewsCount).reversed())
                .collect(Collectors.toList());
    }

}
