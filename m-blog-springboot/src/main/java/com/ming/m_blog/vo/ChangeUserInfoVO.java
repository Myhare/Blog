package com.ming.m_blog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 修改用户个人信息VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserInfoVO {

    @NotNull
    @ApiModelProperty(name = "nickName",value = "用户昵称",required = true,dataType = "String")
    public String nickName;

    @NotNull
    @ApiModelProperty(name = "nickName",value = "用户昵称",required = true,dataType = "String")
    public String intro;

}
