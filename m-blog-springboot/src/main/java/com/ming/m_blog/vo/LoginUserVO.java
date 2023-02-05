package com.ming.m_blog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NotNull
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserVO {

    @NotNull
    @Min(value = 6,message = "用户名不能小于6位")
    @ApiModelProperty(name = "username",value = "用户名",required = true,dataType = "String")
    public String username;

    @NotNull
    @Min(value = 6,message = "密码不能小于6位")
    @ApiModelProperty(name = "password",value = "密码",required = true,dataType = "String")
    public String password;

}
