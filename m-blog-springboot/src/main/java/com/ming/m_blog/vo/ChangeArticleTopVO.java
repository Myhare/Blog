package com.ming.m_blog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 修改文章置顶状况VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeArticleTopVO {

    /**
     * 文章id
     */
    @NotNull
    @ApiModelProperty(name = "articleId", value = "文章id", dataType = "Integer")
    private Integer articleId;

    /**
     * 要修改的指定情况
     */
    @NotNull
    @ApiModelProperty(name = "isTop", value = "要修改的指定情况", dataType = "Integer")
    private Integer isTop;

}
