package com.ming.m_blog.constant;

/**
 * redis前缀点赞
 * @author liuziming
 * @date 2022/10/8
 */
public class RedisPrefixConst {

    /**
     * 用户文章点赞集合
     */
    public static final String ARTICLE_USER_LIKE = "article_user_like:";

    /**
     * 文章点赞数量
     * Hash格式(文章id，点赞数量)
     */
    public static final String ARTICLE_LIKE_COUNT = "article_like_count";

    /**
     * 文章阅读数量
     * Hash格式(文章id，点赞数量)
     */
    public static final String ARTICLE_VIEW_COUNT = "article_view_count";

    /**
     * 用户评论点赞集合
     */
    public static final String COMMENT_USER_LIKE = "comment_user_like:";

    /**
     * 评论点赞数量
     * Hash格式 (评论id，点赞数量)
     */
    public static final String COMMENT_LIKE_COUNT = "comment_like_count";

    /**
     * 博客基本信息 WebsiteConfig对象
     */
    public static final String WEBSITE_CONFIG = "website_config";

    /**
     * 后台登录token
     */
    public static final String BACKSTAGE_LOGIN_TOKEN = "backstage_login_token:";

    /**
     * 博客总访问量
     */
    public static final String BLOG_VIEW_COUNT = "blog_view_count";

    /**
     * 用户地区
     */
    public static final String USER_AREA = "user_area";

    /**
     * 访客信息，存访问网站的用户集合
     */
    public static final String UNIQUE_VISITOR = "unique_visitor";

    /**
     * 访客地区,存访问网站的用户位置
     * Hash格式(ip地址，访问次数)
     */
    public static final String VISITOR_AREA = "visitor_area";

    /**
     * 接口限流前缀
     */
    public static final String ACCESS_LIMIT = "access_limit:";

    /**
     * 验证码
     * 前缀:用户邮箱
     */
    public static final String REGISTER_CODE = "register_code:";


}
