package com.ming.m_blog.dto.category;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 简单分类信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategorySimpleDTO {

    /**
     * 文章id
     */
    private Integer id;

    /**
     * 文章名称
     */
    private String categoryName;

}
