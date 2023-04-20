package com.ming.m_blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "网站配置")
public class WebsiteConfigVO {

    /**
     * 网站作者
     */
    @ApiModelProperty(name = "websiteAuthor", value = "网站作者", required = true, dataType = "String")
    private String websiteAuthor;

    /**
     * 网站名称
     */
    @ApiModelProperty(name = "websiteName", value = "网站名称", required = true, dataType = "String")
    private String websiteName;

    /**
     * 网站头像
     */
    @ApiModelProperty(name = "websiteAvatar", value = "网站头像", required = true, dataType = "String")
    private String websiteIcon;

    /**
     * 网站介绍
     */
    @ApiModelProperty(name = "websiteIntro", value = "网站介绍", required = true, dataType = "String")
    private String websiteIntro;

    /**
     * 网站公告
     */
    @ApiModelProperty(name = "websiteNotice", value = "网站公告", required = true, dataType = "String")
    private String websiteNotice;

    /**
     * 网站创建时间
     */
    @ApiModelProperty(name = "websiteCreateTime", value = "网站创建时间", required = true, dataType = "LocalDateTime")
    private String websiteCreateTime;

    /**
     * 游客头像
     */
    @ApiModelProperty(name = "touristAvatar", value = "游客头像", required = true, dataType = "String")
    private String touristAvatar;

    /**
     * 用户头像
     */
    @ApiModelProperty(name = "userAvatar", value = "用户头像", required = true, dataType = "String")
    private String userAvatar;

    /**
     * websocket路径
     */
    @ApiModelProperty(name = "websocketUrl", value = "websocket路径",required = true,dataType = "String")
    private String websocketUrl;

    /**
     * 是否开启聊天室
     */
    @ApiModelProperty(name = "isReward", value = "是否开启聊天室", required = true, dataType = "Integer")
    private Integer isChatRoom;

    /**
     * 是否评论审核
     */
    @ApiModelProperty(name = "isCommentReview", value = "是否评论审核 1需要 0不需要", required = true, dataType = "Integer")
    private Integer isCommentReview;

    /**
     * 是否留言审核
     */
    @ApiModelProperty(name = "isMessageReview", value = "是否留言审核", required = true, dataType = "Integer")
    private Integer isMessageReview;

    /**
     * 是否邮箱通知
     */
    @ApiModelProperty(name = "isEmailNotice", value = "是否邮箱通知", required = true, dataType = "Integer")
    private Integer isEmailNotice;

    /**
     * 社交url列表
     */
    @ApiModelProperty(name = "isSocialList", value = "社交url列表", required = true, dataType = "Integer")
    private List<String> socialUrlList;

    /**
     * qq
     */
    @ApiModelProperty(name = "qq", value = "qq", required = true, dataType = "String")
    private String qq;

    /**
     * github
     */
    @ApiModelProperty(name = "github", value = "github", required = true, dataType = "String")
    private String github;

    /**
     * gitee
     */
    @ApiModelProperty(name = "gitee", value = "我的gitee", required = true, dataType = "String")
    private String gitee;


    /**
     * 网站备案号
     */
    @ApiModelProperty(name = "websiteRecordNo", value = "网站备案号", required = true, dataType = "String")
    private String websiteRecordNo;

    /**
     * 第三方登录开启列表
     */
    @ApiModelProperty(name = "socialLoginList", value = "第三方登录开启列表", required = true, dataType = "String")
    private List<String> socialLoginList;

}
