package com.ming.m_blog.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询评论列表VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCommentsVO {

    @ApiModelProperty(name = "type", value = "评论回答的类型",required = true,dataType = "Integer")
    private Integer type;

    @ApiModelProperty(name = "review", value = "是否审核",required = true,dataType = "Integer")
    private Integer review;

    @ApiModelProperty(name = "pageNum",value = "评论分页页码",required = true,dataType = "Integer")
    private Integer pageNum;

    @ApiModelProperty(name = "pageSize",value = "一页显示数量",required = true,dataType = "Integer")
    private Integer pageSize;

    @ApiModelProperty(name = "keywords",value = "搜索关键字",required = true,dataType = "String")
    private String keywords;

    @ApiModelProperty(name = "isDelete",value = "是否删除",required = true,dataType = "String")
    private Integer isDelete;

}
