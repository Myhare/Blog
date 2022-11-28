package com.ming.m_blog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    @ApiModelProperty(name = "recordList",value = "分页对象",required = true,dataType = "String")
    private List<T> recordList;

    @ApiModelProperty(name = "count",value = "所有数量",required = true,dataType = "Integer")
    private Integer count;

}
