/*
 Navicat Premium Data Transfer

 Source Server         : MySQL8_3306
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3306
 Source Schema         : blog

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 23/05/2023 16:27:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NOT NULL COMMENT '作者id',
  `category_id` int NULL DEFAULT NULL COMMENT '文章分类',
  `cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'http://124.223.117.228:81/blogGetDefaultFile/deafaultArticleCover.png' COMMENT '文章封面图',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章内容',
  `type` tinyint(1) NOT NULL COMMENT '文章类型 1原创 2转载',
  `is_top` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否置顶 1是 0否',
  `is_delete` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 1是 0否',
  `status` tinyint(1) NOT NULL COMMENT '状态 0草稿 1公开 2私密',
  `create_time` datetime NOT NULL COMMENT '发布时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES (17, 1, 1, 'http://127.0.0.1/blogUploadFile/articles/339153e93d6346e2ad4182ef505df758.jpg', '测试文章(2022-09-04)', '# 第一标题\n## 第二标题\n这是内容\n# 第二个第一标题\n## 图片\n![史尔特尔 明日方舟4k动漫壁纸_彼岸图网.jpg](http://127.0.0.1/blogUploadFile/articles/bd7f69753b834f5291375d869b08ae42.jpg)', 1, 1, 0, 1, '2022-09-04 09:35:07', '2023-04-09 11:13:18');
INSERT INTO `article` VALUES (18, 1, 19, 'http://127.0.0.1/blogUploadFile/articles/33d48cf99b744a5795866d270a557a89.jpg', 'ThreadLocal对象', '# 线程中ThreadLocal模型\n![image.png](http://127.0.0.1/blogUploadFile/articles/8f3ab4cb9fbe46a4a2fb5cac6cdf4d7c.png)\n# ThreadLocalMap对象\nThreadLocalMap是ThreadLocal的一个内部类\n在每一个Thread线程中，都有一个ThreadLocalMap对象，里面使用ThreadLocal作为Key，用户可以自己存入数据当做Value\n```\npublic class MyThreadLocal {\n    private static ThreadLocal<String> sThreadLocal = new ThreadLocal<>();\n    public static void main(String args[]) {\n        sThreadLocal.set(\"这是在主线程中\");\n        System.out.println(\"线程名字：\" + Thread.currentThread().getName() + \"---\" + sThreadLocal.get());\n        //线程a\n        new Thread(new Runnable() {\n            @Override\n            public void run() {\n                sThreadLocal.set(\"这是在线程a中\");\n                System.out.println(\"线程名字：\" + Thread.currentThread().getName() + \"---\" + sThreadLocal.get());\n            }\n        }, \"线程a\").start();\n        //线程b\n        new Thread(new Runnable() {\n            @Override\n            public void run() {\n                sThreadLocal.set(\"这是在线程b中\");\n                System.out.println(\"线程名字：\" + Thread.currentThread().getName() + \"---\" + sThreadLocal.get());\n            }\n        }, \"线程b\").start();\n        //线程c\n        new Thread(() -> {\n            sThreadLocal.set(\"这是在线程c中\");\n            System.out.println(\"线程名字：\" + Thread.currentThread().getName() + \"---\" + sThreadLocal.get());\n        }, \"线程c\").start();\n    }\n}\n```\n上面代码运行结果：\n![image.png](http://127.0.0.1/blogUploadFile/articles/6da2a9ad01c44240b26704447aadf4f7.png)\n**因为每一个线程中都有一个ThreadLocalMap对象，所以在多个线程中使用ThreadLocal进行value值传输的时候不会发生脏读现象。**\n# ThreadLocal对象\n## 核心方法\n- set()方法用于保存当前线程的副本变量值。\n- get()方法用于获取当前线程的副本变量值。\n- initialValue()为当前线程初始副本变量值。\n- remove()方法移除当前线程的副本变量值。\n可以将ThreadLocal对象作为key往ThreadLocalMap传递想要存入的数据。可以在工具类中定义一个static静态的ThreadLocal独享，防止多个线程创建同一个对象浪费资源。同时因为每个线程的ThreadLocalMap是不同的，所以即使key相同也不会发生脏读现象。\n比如创建一个分页工具类，用户发起分页请求之后可以将Page对象传入ThreadLocal中，之后可以在请求的过程直接通过工具类获取Page对象\n```\n/**\n * 分页工具类\n */\npublic class PageUtils {\n\n    public static final ThreadLocal<Page<?>> PAGE_HOLDER = new ThreadLocal<>();\n\n    // 设置分页页码\n    public static void setPageNum(Page<?> page){\n        PAGE_HOLDER.set(page);\n    }\n\n    // 获取分页信息\n    public static Page<?> getPage(){\n        Page<?> page = PAGE_HOLDER.get();\n        // 如果当前线程没有page对象，新建一个对象，并且new一个Page存进去，new一个Page会有MybatisPlus的默认值\n        if (Objects.isNull(page)){\n            setPageNum(new Page<>());\n        }\n        return PAGE_HOLDER.get();\n    }\n\n    public static Long getPageNum() {\n        return getPage().getCurrent();\n    }\n\n    public static Long getSize() {\n        return getPage().getSize();\n    }\n\n    public static Long getLimitCurrent() {\n        return (getPageNum() - 1) * getSize();\n    }\n\n    public static void remove() {\n        PAGE_HOLDER.remove();\n    }\n\n}\n```\n**Springsecurity中的SecurityContextHolder就是将用户信息存入ThreadLocal中的，所以在使用SpringSecurity的时候如果想要获取登录用户信息不能在子线程中获取**', 1, 0, 0, 1, '2022-10-04 22:55:28', '2022-11-09 19:24:47');
INSERT INTO `article` VALUES (19, 1, 1, 'http://www.static.mingzib.xyz/blogGetDefaultFile/deafaultArticleCover.png', '测试各种东西的文章(2022-11-17)', '# 测试标题1\n![illust_8380932_20200828_151454.jpg](http://127.0.0.1/blogUploadFile/articles/be2b78519ac34cccbe2abafcf4bc7c3e.jpg)\n## 测试二级标题1\n![5ad96d4f45d24e116bbb163868568299.jpg](http://127.0.0.1/blogUploadFile/articles/e586593dc982495bb78f652a6d45a8da.jpg)\n## 测试二级标题2\n![1608719026003.jpeg](http://127.0.0.1/blogUploadFile/articles/95fd172092b74be9ab5c6d0f52f8d6e7.jpeg)\n# 测试标题2\n## 测试代码\n```java\n    // 测试密码加密\n    @Test\n    void testBCryptPasswordEncoder() {\n        // SpringSecurity底层中使用的是下面的加密方法\n        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();\n        String encode1 = passwordEncoder.encode(\"123456\");  // 传入密码的原文，返回加密后的字符串\n        // String encode2 = passwordEncoder.encode(\"123456\");\n        System.out.println(encode1);\n        // System.out.println(encode2);   // 每一次自动生成的对象都是不一样的\n        // 校验密码\n        // 校验密码的时候需要传入加密后的字符串（数据库中查找）,和用户输入的明文密码进行比较，如果一致返回true，否则返回false\n        boolean matches = passwordEncoder.matches(\"123456\", \"$2a$10$HRmP/ugSgqXaTv7TzvylmuHsgqOytTAugEd7mdG5PHwMI0oJb1jh6\");\n        System.out.println(matches);\n    }\n```\n\n```xml\n<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n    <modelVersion>4.0.0</modelVersion>\n    <groupId>com.ming</groupId>\n    <artifactId>demo</artifactId>\n    <version>0.0.1-SNAPSHOT</version>\n    <name>m-blog-springboot</name>\n    <description>个人博客项目</description>\n</project>\n```', 1, 0, 0, 1, '2022-11-17 18:02:32', '2023-01-11 10:37:05');
INSERT INTO `article` VALUES (21, 1, 20, 'http://www.static.mingzib.xyz/blogGetDefaultFile/deafaultArticleCover.png', '测试草稿', '111', 1, 0, 1, 0, '2023-03-16 14:52:25', '2023-03-16 14:53:49');
INSERT INTO `article` VALUES (22, 1, 21, 'http://www.static.mingzib.xyz/blogGetDefaultFile/deafaultArticleCover.png', '测试发布文章', '11111', 1, 0, 1, 0, '2023-03-16 14:58:47', '2023-03-16 14:59:11');
INSERT INTO `article` VALUES (23, 1, NULL, 'http://www.static.mingzib.xyz/blogGetDefaultFile/deafaultArticleCover.png', '测试发布草稿', '哈哈哈', 1, 0, 1, 0, '2023-03-16 15:08:27', '2023-03-16 15:08:27');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '测试分类', '2022-08-14 15:47:44', '2022-08-14 15:47:44');
INSERT INTO `category` VALUES (16, '测试添加博客分类名称', '2022-08-19 21:48:21', '2022-08-19 21:48:21');
INSERT INTO `category` VALUES (18, '上传图片', '2022-09-04 09:35:07', '2022-09-04 09:35:07');
INSERT INTO `category` VALUES (19, '复习知识点', '2022-10-04 22:55:28', '2022-10-04 22:55:28');

-- ----------------------------
-- Table structure for chat_record
-- ----------------------------
DROP TABLE IF EXISTS `chat_record`;
CREATE TABLE `chat_record`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户头像',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '聊天内容',
  `ip_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip来源',
  `type` tinyint NOT NULL COMMENT '消息类型',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_record
-- ----------------------------
INSERT INTO `chat_record` VALUES (33, NULL, '127.0.0.1', 'http://www.static.mingzib.xyz/blogUploadFile/configImage/2321289b923b4f149dbdea6bda4def75.jpg', '烦死', '127.0.0.1', '本地', 3, '2023-02-07 17:12:47', '2023-02-07 17:12:47');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NOT NULL COMMENT '发布用户id(userAuthId)',
  `reply_user_id` int NULL DEFAULT NULL COMMENT '回复用户id(userAuthId)',
  `topic_id` int NULL DEFAULT NULL COMMENT '主题id',
  `comment_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `parent_id` int NULL DEFAULT NULL COMMENT '父评论id',
  `type` tinyint NOT NULL COMMENT '类型 1文章 2友链 3说说',
  `is_delete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除 0否 1是',
  `is_review` tinyint NOT NULL DEFAULT 0 COMMENT '是否审核 0否 1是',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (4, 1, NULL, 17, '测试评论', NULL, 1, 0, 1, '2022-09-27 20:04:46', '2023-02-14 16:02:34');

-- ----------------------------
-- Table structure for in_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `in_article_tag`;
CREATE TABLE `in_article_tag`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tag_id` int NOT NULL COMMENT '标签id',
  `article_id` int NOT NULL COMMENT '文章id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of in_article_tag
-- ----------------------------
INSERT INTO `in_article_tag` VALUES (1, 1, 1);
INSERT INTO `in_article_tag` VALUES (3, 11, 4);
INSERT INTO `in_article_tag` VALUES (4, 12, 4);
INSERT INTO `in_article_tag` VALUES (5, 13, 4);
INSERT INTO `in_article_tag` VALUES (7, 1, 5);
INSERT INTO `in_article_tag` VALUES (8, 1, 6);
INSERT INTO `in_article_tag` VALUES (9, 11, 7);
INSERT INTO `in_article_tag` VALUES (10, 1, 11);
INSERT INTO `in_article_tag` VALUES (11, 1, 12);
INSERT INTO `in_article_tag` VALUES (12, 1, 14);
INSERT INTO `in_article_tag` VALUES (13, 1, 15);
INSERT INTO `in_article_tag` VALUES (14, 1, 11);
INSERT INTO `in_article_tag` VALUES (15, 1, 11);
INSERT INTO `in_article_tag` VALUES (16, 14, 16);
INSERT INTO `in_article_tag` VALUES (17, 15, 17);
INSERT INTO `in_article_tag` VALUES (19, 16, 18);
INSERT INTO `in_article_tag` VALUES (23, 1, 19);
INSERT INTO `in_article_tag` VALUES (24, 1, 19);

-- ----------------------------
-- Table structure for in_role_power
-- ----------------------------
DROP TABLE IF EXISTS `in_role_power`;
CREATE TABLE `in_role_power`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int NOT NULL COMMENT '角色id',
  `power_id` int NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of in_role_power
-- ----------------------------
INSERT INTO `in_role_power` VALUES (1, 1, 1);
INSERT INTO `in_role_power` VALUES (2, 2, 2);

-- ----------------------------
-- Table structure for in_user_role
-- ----------------------------
DROP TABLE IF EXISTS `in_user_role`;
CREATE TABLE `in_user_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NOT NULL COMMENT '用户id，这里值得是userInfo的id',
  `role_id` int NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of in_user_role
-- ----------------------------
INSERT INTO `in_user_role` VALUES (1, 1, 1);
INSERT INTO `in_user_role` VALUES (2, 1, 2);
INSERT INTO `in_user_role` VALUES (4, 3, 2);
INSERT INTO `in_user_role` VALUES (5, 5, 2);
INSERT INTO `in_user_role` VALUES (6, 6, 2);
INSERT INTO `in_user_role` VALUES (7, 7, 2);
INSERT INTO `in_user_role` VALUES (8, 8, 2);
INSERT INTO `in_user_role` VALUES (9, 9, 2);
INSERT INTO `in_user_role` VALUES (10, 10, 2);
INSERT INTO `in_user_role` VALUES (11, 11, 2);
INSERT INTO `in_user_role` VALUES (18, 20, 2);

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '头像',
  `message_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '留言内容',
  `ip_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ip',
  `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户地址',
  `time` tinyint(1) NULL DEFAULT NULL COMMENT '弹幕速度',
  `is_review` tinyint NOT NULL DEFAULT 1 COMMENT '是否审核',
  `create_time` datetime NOT NULL COMMENT '发布时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (1, '游客', 'http://www.static.mingzib.xyz/blogUploadFile/configImage/2321289b923b4f149dbdea6bda4def75.jpg', '测试留言', '127.0.0.1', '本地', 8, 0, '2023-02-09 10:49:40', '2023-02-09 10:49:40');

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `opt_module` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作模块',
  `opt_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型',
  `opt_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作url',
  `opt_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作方法',
  `opt_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作描述',
  `request_param` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求参数',
  `request_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求方式',
  `response_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '返回数据',
  `user_id` int NOT NULL COMMENT '用户id',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `ip_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作ip',
  `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作地址',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for page
-- ----------------------------
DROP TABLE IF EXISTS `page`;
CREATE TABLE `page`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键，页面id',
  `page_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '页面名',
  `page_label` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '页面标签',
  `page_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '页面封面',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of page
-- ----------------------------
INSERT INTO `page` VALUES (1, '主页', 'home', 'http://127.0.0.1/blogUploadFile/configImage/8f2763da0ae544c7bf6b813670d2cbb5.jpg', '2022-11-08 08:58:35', '2022-11-11 16:24:20');
INSERT INTO `page` VALUES (2, '标签', 'tag', 'http://127.0.0.1/blogUploadFile/configImage/98924531992c4202b05d44b4974d2815.png', '2022-11-08 08:59:00', '2023-02-13 15:17:14');
INSERT INTO `page` VALUES (3, '用户信息', 'user', 'http://127.0.0.1/blogUploadFile/configImage/34602bc541c34673a1c581e868c1b891.png', '2022-11-08 08:59:18', '2022-11-11 16:24:47');
INSERT INTO `page` VALUES (4, '分类', 'category', 'http://127.0.0.1/blogUploadFile/configImage/21d009d041954842b0ad67c98e857ca1.png', '2022-11-08 08:59:48', '2022-11-11 16:25:20');
INSERT INTO `page` VALUES (5, '文章列表', 'articleList', 'http://127.0.0.1/blogUploadFile/configImage/BD51B4F3B936D1638C97EA080FA05A4B.jpeg', '2022-11-08 09:01:06', '2023-04-13 10:37:04');
INSERT INTO `page` VALUES (6, '归档', 'archive', 'http://127.0.0.1/blogUploadFile/configImage/04b81b0d76564f7a9a37bfc6ceb427d4.jpg', '2022-11-08 09:01:30', '2023-02-13 15:17:26');
INSERT INTO `page` VALUES (7, '留言', 'message', 'http://www.static.mingzib.xyz/blogUploadFile/articles/ebc2646a962141b38a70461f3c1e4318.jpg', '2023-02-11 10:48:31', NULL);
INSERT INTO `page` VALUES (8, '说说', 'talks', 'http://127.0.0.1/blogUploadFile/configImage/B9DE7DDE736B4C7FA002BE17B83107E1.png', '2023-05-20 10:33:12', '2023-05-20 10:39:02');

-- ----------------------------
-- Table structure for power
-- ----------------------------
DROP TABLE IF EXISTS `power`;
CREATE TABLE `power`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `perms` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限标识',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `updata_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of power
-- ----------------------------
INSERT INTO `power` VALUES (1, 'sys:admin', NULL, NULL);
INSERT INTO `power` VALUES (2, 'sys:user', NULL, NULL);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_lable` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `is_delete` int NOT NULL DEFAULT 0 COMMENT '是否禁用(0表示正常，1表示禁用)',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 'admin', '大管理员,拥有全部权限', 0, NULL, NULL);
INSERT INTO `role` VALUES (2, 'user', '普通用户，只能查看博客', 0, NULL, NULL);

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tag_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tag
-- ----------------------------
INSERT INTO `tag` VALUES (1, '测试标签', '2022-08-15 21:01:39', '2022-08-15 21:30:05');
INSERT INTO `tag` VALUES (11, '111', '2022-08-19 21:48:22', '2022-08-19 21:48:22');
INSERT INTO `tag` VALUES (12, '222', '2022-08-19 21:48:22', '2022-08-19 21:48:22');
INSERT INTO `tag` VALUES (13, '333', '2022-08-19 21:48:22', '2022-08-19 21:48:22');
INSERT INTO `tag` VALUES (14, '新的标签', '2022-08-30 15:54:46', '2022-08-30 15:54:46');
INSERT INTO `tag` VALUES (15, '图片', '2022-09-04 09:35:07', '2022-09-04 09:35:07');
INSERT INTO `tag` VALUES (16, 'ThreadLocal', '2022-10-04 22:55:28', '2022-10-04 22:55:28');

-- ----------------------------
-- Table structure for talk
-- ----------------------------
DROP TABLE IF EXISTS `talk`;
CREATE TABLE `talk`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '说说id',
  `user_id` int NOT NULL COMMENT '用户id',
  `content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '说说内容',
  `images` varchar(2500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片',
  `is_top` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否置顶',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态 1.公开 2.私密',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 56 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of talk
-- ----------------------------
INSERT INTO `talk` VALUES (49, 1, '测试说说<img src=\"http://www.static.mingzib.xyz/emoji/goutou.png\" width=\"24\" height=\"24\" alt=\"[狗头]\" style=\"margin: 0 1px;vertical-align: text-bottom\">', NULL, 0, 1, '2022-01-24 23:34:59', NULL);

-- ----------------------------
-- Table structure for unique_view
-- ----------------------------
DROP TABLE IF EXISTS `unique_view`;
CREATE TABLE `unique_view`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `views_count` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '访问量',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of unique_view
-- ----------------------------
INSERT INTO `unique_view` VALUES (1, '1', '2022-11-15 20:48:24', '2022-11-15 20:48:24');
INSERT INTO `unique_view` VALUES (2, '5', '2022-12-19 00:00:00', '2022-12-19 00:00:00');

-- ----------------------------
-- Table structure for user_auth
-- ----------------------------
DROP TABLE IF EXISTS `user_auth`;
CREATE TABLE `user_auth`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_info_id` int NOT NULL COMMENT '用户信息id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名	',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户密码',
  `login_type` tinyint(1) NOT NULL COMMENT '登录类型 1邮箱或用户名登录，2QQ登录',
  `ip_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `ip_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip来源',
  `login_time` datetime NULL DEFAULT NULL COMMENT '上次登录时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_auth
-- ----------------------------
INSERT INTO `user_auth` VALUES (1, 1, '11111', '$2a$10$O46kH89dlrHG5djxJcoKwu47fIdKGgxwMXxhIrS/GHgzRDTMYyieq', 1, '127.0.0.1', '本地', '2023-05-20 20:46:59', '2022-08-07 21:13:41', '2023-05-20 20:46:59');
INSERT INTO `user_auth` VALUES (2, 3, '22222', '$2a$10$Nz2HaJhCgX/yP.z3.vybv.Nhtgiy8oIOVTb4uNtK41MtnMn.KL3Sq', 1, '127.0.0.1', '本地', '2023-05-02 20:38:09', '2022-08-07 09:46:52', '2023-05-02 20:38:09');
INSERT INTO `user_auth` VALUES (3, 5, '1146719649@qq.com', '$2a$10$Tf7AW2ctYiqWAMVRmQVWuOtAFGGdc8FaSavUL6jcDG5RbjaYWzqde', 1, '127.0.0.1', '本地', '2023-05-02 20:36:53', '2023-01-18 11:43:06', '2023-05-02 20:36:53');
INSERT INTO `user_auth` VALUES (4, 6, '1940307627@qq.com', '$2a$10$Po7Bnys1zknjs3O1vJluB.nk/Z651nbRzbI/.Gc4C.uO11aSYxq7u', 1, NULL, NULL, NULL, '2023-01-18 21:00:28', '2023-01-18 21:00:28');
INSERT INTO `user_auth` VALUES (5, 7, '1940307627@qq.com', '$2a$10$lrqRPCvYfUGwByDvNdHLUeVfNUJcj583Jq30eA4FKdRu5PjDk4Oym', 1, NULL, NULL, NULL, '2023-01-18 21:06:35', '2023-01-18 21:06:35');
INSERT INTO `user_auth` VALUES (6, 8, '1940307627@qq.com', '$2a$10$3OLSMPxnw7GVCaH9JQL2KuMsdJeunagc3HBz29dUjapeEf7Wd1hUS', 1, NULL, NULL, NULL, '2023-01-18 21:08:13', '2023-01-18 21:08:13');
INSERT INTO `user_auth` VALUES (7, 9, '1940307627@qq.com', '$2a$10$cmJKiYE2XScfzUdqjvQOo.W5hYW7MAiyHi/ePldxugHPM4cCeCBR6', 1, '127.0.0.1', '本地', '2023-01-18 21:09:33', '2023-01-18 21:09:31', '2023-01-18 21:09:33');
INSERT INTO `user_auth` VALUES (8, 10, '1940307627@qq.com', '$2a$10$w1n71tkGyRCwGoLhXEtHDOJ6fwiCz7ElD4zUv1EN/lCxXJTG3b5VW', 1, '127.0.0.1', '本地', '2023-01-18 21:12:19', '2023-01-18 21:12:19', '2023-01-18 21:12:19');
INSERT INTO `user_auth` VALUES (9, 11, '1940307627@qq.com', '$2a$10$FPHcBEaow1FpErhZLR9EGenEIolTmez74JtNx48aTzszcDf7Jwv1u', 1, '127.0.0.1', '本地', '2023-01-18 21:14:27', '2023-01-18 21:14:26', '2023-01-18 21:14:27');
INSERT INTO `user_auth` VALUES (16, 20, '414847D8A4F48D5177A9A094550ADB80', '51463442A839B4F0FEAD84FF5FD43861', 2, '127.0.0.1', '本地', '2023-04-20 15:19:30', '2023-04-20 15:19:30', '2023-04-20 15:19:30');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'http://124.223.117.228:81/blogGetDefaultFile/defaultAvatar.jpg' COMMENT '获取用户头像路径',
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户简介',
  `is_delete` int NULL DEFAULT 0 COMMENT '是否被封禁(0表示正常，1表示被封禁)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (1, '1940307627@qq.com', '博客扛把子', 'http://127.0.0.1/blogUploadFile/articles/E06A56233BD15C357265FF3A58167CF9.jpg', '这里是嘚嘚的个人简介', 0, '2022-08-04 22:20:40', '2023-05-10 08:46:11');
INSERT INTO `user_info` VALUES (3, '123@qq.com', 'dede2', 'http://127.0.0.1/blogUploadFile/avatar/ccaedea187e24fb5890155cb50a1e080.jpg', '暂无', 0, '2022-08-07 09:46:17', '2023-05-02 20:05:29');
INSERT INTO `user_info` VALUES (5, '1146719649@qq.com', '用户1615555349320941570', 'http://www.static.mingzib.xyz/blogUploadFile/configImage/2321289b923b4f149dbdea6bda4def75.jpg', NULL, 0, '2023-01-18 11:43:06', '2023-01-18 11:43:06');
INSERT INTO `user_info` VALUES (6, '19403076271@qq.com', '用户1615695617297494018', 'http://www.static.mingzib.xyz/blogUploadFile/configImage/2321289b923b4f149dbdea6bda4def75.jpg', NULL, 0, '2023-01-18 21:00:28', '2023-01-18 21:00:28');
INSERT INTO `user_info` VALUES (7, '19403076272@qq.com', '用户1615697155210358785', 'http://www.static.mingzib.xyz/blogUploadFile/configImage/2321289b923b4f149dbdea6bda4def75.jpg', NULL, 0, '2023-01-18 21:06:35', '2023-01-18 21:06:35');
INSERT INTO `user_info` VALUES (8, '19403076273@qq.com', '用户1615697566189236226', 'http://www.static.mingzib.xyz/blogUploadFile/configImage/2321289b923b4f149dbdea6bda4def75.jpg', NULL, 0, '2023-01-18 21:08:13', '2023-01-18 21:08:13');
INSERT INTO `user_info` VALUES (9, '19403076274@qq.com', '用户1615697896570368001', 'http://www.static.mingzib.xyz/blogUploadFile/configImage/2321289b923b4f149dbdea6bda4def75.jpg', NULL, 0, '2023-01-18 21:09:31', '2023-01-18 21:09:31');
INSERT INTO `user_info` VALUES (10, '19403076275@qq.com', '用户1615698597576978433', 'http://www.static.mingzib.xyz/blogUploadFile/configImage/2321289b923b4f149dbdea6bda4def75.jpg', NULL, 0, '2023-01-18 21:12:19', '2023-01-18 21:12:19');
INSERT INTO `user_info` VALUES (11, '19403076276@qq.com', '用户1615699133755830274', 'http://www.static.mingzib.xyz/blogUploadFile/configImage/2321289b923b4f149dbdea6bda4def75.jpg', NULL, 0, '2023-01-18 21:14:26', '2023-01-18 21:14:26');
INSERT INTO `user_info` VALUES (20, NULL, '安全圈', 'http://thirdqq.qlogo.cn/g?b=oidb&k=KgIvAibbFykxE0RU6Kst4uQ&kti=ZEDnhQAAAAI&s=40&t=1660001368', NULL, 0, '2023-04-20 15:19:30', '2023-04-20 15:19:30');

-- ----------------------------
-- Table structure for website_config
-- ----------------------------
DROP TABLE IF EXISTS `website_config`;
CREATE TABLE `website_config`  (
  `id` int NOT NULL COMMENT '主键',
  `config` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据库基本信息(JSON格式)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of website_config
-- ----------------------------
INSERT INTO `website_config` VALUES (1, '{\"gitee\":\"https://gitee.com/catMountain\",\"github\":\"https://github.com/Myhare/Blog\",\"isChatRoom\":1,\"isCommentReview\":0,\"isEmailNotice\":0,\"isMessageReview\":1,\"qq\":\"1940307627\",\"socialLoginList\":[\"qq\"],\"socialUrlList\":[\"github\",\"gitee\"],\"touristAvatar\":\"http://www.static.mingzib.xyz/blogUploadFile/configImage/2321289b923b4f149dbdea6bda4def75.jpg\",\"userAvatar\":\"http://127.0.0.1/blogUploadFile/configImage/b92abfd3ad844af7b231958a919e2e51.jpg\",\"websiteAuthor\":\"Ming\",\"websiteCreateTime\":\"2022-8-20\",\"websiteIcon\":\"http://127.0.0.1/blogUploadFile/icon/395e729c60fe4d61815b9fe29f21b7bb.png\",\"websiteIntro\":\"这是网站个人简介\",\"websiteName\":\"Ming的个人博客\",\"websiteNotice\":\"欢迎来到我的个人博客\",\"websiteRecordNo\":\"赣ICP备2022004397号-1\",\"websocketUrl\":\"ws://127.0.0.1/chatRoom\"}', '2022-10-27 09:09:29', '2023-05-02 20:22:55');

SET FOREIGN_KEY_CHECKS = 1;
