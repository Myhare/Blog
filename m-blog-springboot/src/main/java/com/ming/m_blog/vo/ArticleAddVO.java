package com.ming.m_blog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 添加博客VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleAddVO {

    @ApiModelProperty(name = "id", value = "主键",required = true,dataType = "Integer")
    private Integer id;

    @NotNull
    @ApiModelProperty(name = "articleTitle", value = "博客标题",required = true,dataType = "String")
    private String articleTitle;

    @NotNull
    @ApiModelProperty(name = "articleContext",value = "博客内容", required = true,dataType = "String")
    private String articleContent;

    @NotNull
    @ApiModelProperty(name = "categoryName",value = "博客分类名称", required = true, dataType = "String")
    private String categoryName;

    @NotNull
    @ApiModelProperty(name = "tagNameList", value = "博客标签列表", required = true,dataType = "List")
    private List<String> tagList;

    @NotNull
    @ApiModelProperty(name = "articleType", value = "博客类型", required = true, dataType = "Integer")
    private Integer articleType;

    @NotNull
    @ApiModelProperty(name = "coverUrl", value = "博客封面URL", required = true, dataType = "String")
    private String coverUrl;

    @NotNull
    @ApiModelProperty(name = "isTop", value = "是否置顶", required = true, dataType = "Integer")
    private Integer isTop;

    @NotNull
    @ApiModelProperty(name = "status", value = "文章状态", required = true, dataType = "Integer")
    private Integer status;

}
