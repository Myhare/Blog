package com.ming.m_blog.controller;


import com.ming.m_blog.dto.UserAreaDTO;
import com.ming.m_blog.dto.UserInfoDTO;
import com.ming.m_blog.mapper.UserAuthMapper;
import com.ming.m_blog.service.UserAuthService;
import com.ming.m_blog.vo.LoginUserVO;
import com.ming.m_blog.vo.RegisterVO;
import com.ming.m_blog.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Ming
 * @since 2022-07-25
 */
@Api(tags = "用户账号验证模块")
@RestController
public class UserAuthController {

    @Autowired
    private UserAuthService userAuthService;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public ResponseResult<?> register(@RequestBody RegisterVO registerVO){
        userAuthService.registerUser(registerVO);
        return ResponseResult.ok();
    }

    @ApiOperation("发送邮件")
    @GetMapping("/sendEmail")
    public ResponseResult<String> sendEmail(String email){
        userAuthService.sendEmail(email);
        return ResponseResult.ok("邮件发送成功，请等待",null);
    }

    @ApiOperation("测试获取用户权限信息")
    @GetMapping("/getUserAuthInfo")
    public String testSecurity(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        return authentication.toString();
    }

    // @PreAuthorize("hasAuthority('sys:admin')")
    @PreAuthorize("hasAuthority('admin')")  // 这里进行用户身份验证，不需要加上"ROLE_"，不过在UserDetails中传递的时候需要加上ROLE_
    @GetMapping("/testAuth")
    public String testSecurity2(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        return authentication.toString();
    }

    @ApiOperation("获取用户区域信息")
    @GetMapping("/admin/user/area")
    public ResponseResult<List<UserAreaDTO>> getUserArea(Integer type){
        return ResponseResult.ok(userAuthService.getUserAreaDTO(type));
    }

}

