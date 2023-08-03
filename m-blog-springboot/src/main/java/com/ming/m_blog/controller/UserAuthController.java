package com.ming.m_blog.controller;


import com.alibaba.fastjson.JSON;
import com.ming.m_blog.annotation.OptLog;
import com.ming.m_blog.constant.OptTypeConstant;
import com.ming.m_blog.dto.user.UserAreaDTO;
import com.ming.m_blog.dto.user.UserInfoDTO;
import com.ming.m_blog.enums.LoginTypeEnum;
import com.ming.m_blog.service.UserAuthService;
import com.ming.m_blog.strategy.context.SocialLoginStrategyContext;
import com.ming.m_blog.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private SocialLoginStrategyContext socialLoginStrategyContext;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public ResponseResult<?> register(@RequestBody RegisterVO registerVO){
        userAuthService.registerUser(registerVO);
        return ResponseResult.ok();
    }

    @ApiOperation("发送邮件")
    @GetMapping("/sendEmail")
    public ResponseResult<String> sendEmail(String email){
        userAuthService.sendCodeEmail(email);
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

    @OptLog(optType = OptTypeConstant.UPDATE)
    @PreAuthorize("hasAuthority('admin')")
    @ApiOperation("后台修改用户密码")
    @PostMapping("/admin/updatePassword")
    public ResponseResult<?> adminUpdatePassword(@RequestBody PasswordVO passwordVO){
        userAuthService.adminUpdatePassword(passwordVO);
        return ResponseResult.ok();
    }

    @ApiOperation("QQ登录")
    @PostMapping("/oauth/qq")
    public ResponseResult<UserInfoDTO> qqLogin(@RequestBody QQLoginVO qqLoginVO){
        // 第三方QQ登录
        UserInfoDTO userInfoDTO =
                socialLoginStrategyContext.executeLoginStrategy(JSON.toJSONString(qqLoginVO), LoginTypeEnum.QQ);
        return ResponseResult.ok(userInfoDTO);
    }

    @ApiOperation("GitHub登录")
    @PostMapping("/oauth/github")
    public ResponseResult<UserInfoDTO> githubLogin(@RequestBody GithubLoginVO githubLoginVO){
        UserInfoDTO userInfoDTO = socialLoginStrategyContext.executeLoginStrategy(JSON.toJSONString(githubLoginVO), LoginTypeEnum.GIT_HUB);
        return ResponseResult.ok(userInfoDTO);
    }

}

