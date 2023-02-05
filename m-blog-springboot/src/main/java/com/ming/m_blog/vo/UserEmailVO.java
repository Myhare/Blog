package com.ming.m_blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 修改用户邮箱绑定
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "修改绑定邮箱对象")
public class UserEmailVO {

    // 发送的验证码
    @ApiModelProperty(name = "code", value = "验证码", required = true, dataType = "String")
    private String code;

    // 修改后的邮箱
    @ApiModelProperty(name = "email", value = "修改后的邮箱", required = true, dataType = "String")
    private String email;

}
