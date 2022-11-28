package com.ming.m_blog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 后台获取文章列表参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminArticlesVO {

    @ApiModelProperty(name = "type", value = "博客类型",required = true,dataType = "Integer")
    private Integer type;

    @ApiModelProperty(name = "status", value = "博客状态",required = true,dataType = "Integer")
    private Integer status;

    @ApiModelProperty(name = "categoryId",value = "博客分类",required = true,dataType = "Integer")
    private Integer categoryId;

    @ApiModelProperty(name = "tagId",value = "博客标签",required = true,dataType = "Integer")
    private Integer tagId;

    @ApiModelProperty(name = "pageNum",value = "博客分页页码",required = true,dataType = "Integer")
    private Integer pageNum;

    @ApiModelProperty(name = "pageSize",value = "一页显示数量",required = true,dataType = "Integer")
    private Integer pageSize;

    @ApiModelProperty(name = "keywords",value = "搜索关键字",required = true,dataType = "String")
    private String keywords;

    @ApiModelProperty(name = "isDelete",value = "是否删除",required = true,dataType = "String")
    private String isDelete;

}
