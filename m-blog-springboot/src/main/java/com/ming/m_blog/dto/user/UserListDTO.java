package com.ming.m_blog.dto.user;

import com.ming.m_blog.pojo.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 后台查询用户列表数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListDTO {

    /**
     * 用户Authid
     */
    private Integer userId;

    /**
     * 用户头像路径
     */
    private String avatar;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 角色列表
     */
    private List<String> roles;

    /**
     * 发布博客数量
     */
    private Integer blogCount;

    /**
     * 是否禁用
     */
    private Integer isDelete;

    /**
     * 账号创建时间
     */
    private Date createTime;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;
}
