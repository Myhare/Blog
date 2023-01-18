package com.ming.m_blog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户注册VO
 */
@Data
public class RegisterVO {

    /**
     * 用户邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(name = "email",value = "用户邮箱",required = true,dataType = "String")
    private String email;

    /**
     * 用户密码
     */
    @Size(min = 6,message = "密码不能小于6位")
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(name = "password",value = "用户密码",required = true,dataType = "String")
    private String password;

    /**
     * 邮箱验证码
     */
    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(name = "code",value = "验证码",required = true,dataType = "String")
    private String code;

}
