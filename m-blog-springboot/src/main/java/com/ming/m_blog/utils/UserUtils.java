package com.ming.m_blog.utils;

import com.ming.m_blog.dto.user.UserDetailDTO;
import com.ming.m_blog.exception.ReRuntimeException;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {

    // 获取当前登录用户信息
    public static UserDetailDTO getLoginUser(){
        try {
            return (UserDetailDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }catch (Exception e){
            throw new ReRuntimeException("请先登录再操作");
        }
    }

    // 获取登录用户id
    public static Integer getLoginUserId(){
        return getLoginUser().getUserId();
    }

    // 获取登录用户信息Id
    public static Integer getLoginUserInfoId(){
        return getLoginUser().getUserInfoId();
    }

}
