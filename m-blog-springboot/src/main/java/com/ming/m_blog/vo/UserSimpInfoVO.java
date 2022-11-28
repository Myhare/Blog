package com.ming.m_blog.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 登陆后获取用户基本信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSimpInfoVO {

    // 角色
    private List<String> roles;

    // 名称
    private String name;

    // 头像
    private String avatar;

    // 简介
    private String introduction;
}
