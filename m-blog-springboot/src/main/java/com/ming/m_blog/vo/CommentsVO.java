package com.ming.m_blog.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户添加评论VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "评论")
public class CommentsVO {

    /**
     * 回复用户的id
     */
    @ApiModelProperty(name = "replyUserId",value = "被回复用户的id",dataType = "Integer")
    private Integer replyUserId;

    /**
     *评论主题id
     */
    @ApiModelProperty(name = "topicId", value = "评论主题id", dataType = "Integer")
    private Integer topicId;

    /**
     * 评论内容
     */
    @NotBlank
    @ApiModelProperty(name = "commentContent",value = "评论详细内容",dataType = "String")
    private String commentContent;

    /**
     * 父评论id
     */
    @ApiModelProperty(name = "parentId",value = "父评论id",dataType = "Integer")
    private Integer parentId;

    /**
     * 评论类型
     */
    @NotNull(message = "评论类型不能为空")
    @ApiModelProperty(name = "type", value = "评论类型", dataType = "Integer")
    private Integer type;

}
