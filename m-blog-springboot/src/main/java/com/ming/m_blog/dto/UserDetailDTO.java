package com.ming.m_blog.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ming.m_blog.constant.CommonConst;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// @Data    这里如果使用@Data，里面重写的equals和hashcode有问题，在查找在线用户时候SessionRegistryImpl中的principals中当key的时候，遍历找不到
@Getter
@Setter
@ToString
@Builder
public class UserDetailDTO implements UserDetails {


    /**
     * 用户账号id(UserAuthId)
     */
    private Integer userId;

    /**
     * 用户信息id
     */
    private Integer userInfoId;

    /**
     * 登录方式
     */
    private Integer loginType;

    /**
     * 邮箱号
     */
    private String email;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户简介
     */
    private String intro;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 是否禁用
     */
    private Integer isDisable;

    /**
     * 用户角色列表
     */
    private List<String> roleList;

    /**
     * 用户权限列表
     */
    private List<String> powerList;

    /**
     * 用户文章点赞列表
     */
    private Set<Object> articleLikeSet;

    /**
     * 用户评论点赞列表
     */
    private Set<Object> commentLikeSet;

    /**
     * 最近登录时间
     */
    private Date loginTime;

    /**
     *
     * 在存入redis中时候，为了安全考虑，redis不会允许存储，这里忽略,之后只需要将permissions存入redis也可以查询到权限
     * @JSONField ( serialize = false)  // fastJSON添加注解后这个属性就不会被序列化到流当中
     */
    @JsonIgnore   // jackJSON忽略这个字段序列化，不然不能反序列化
    private List<SimpleGrantedAuthority> authList;

    /**
     * 获取用户权限列表
     * @return  获取的权限列表
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 如果已经赋值过权限了，直接返回
        if (authList!=null){
            return authList;
        }

        // 赋值权限列表
         authList = powerList.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return this.authList;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 用户是否锁定
    @Override
    public boolean isAccountNonLocked() {
        return this.isDisable == CommonConst.FALSE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int hashCode(){
        return userId.hashCode();
    }

    @Override
    public boolean equals(Object obj){
        return this.toString().equals(obj.toString());
    }


}
