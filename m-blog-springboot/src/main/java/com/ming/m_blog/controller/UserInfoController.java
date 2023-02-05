package com.ming.m_blog.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.ming.m_blog.dto.UserAreaDTO;
import com.ming.m_blog.dto.UserDetailDTO;
import com.ming.m_blog.dto.UserListDTO;
import com.ming.m_blog.dto.UserOnlineDTO;
import com.ming.m_blog.exception.ReRuntimeException;
import com.ming.m_blog.service.FileService;
import com.ming.m_blog.service.UserAuthService;
import com.ming.m_blog.service.UserInfoService;
import com.ming.m_blog.utils.UserUtils;
import com.ming.m_blog.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 用户信息控制器
 * @author Ming
 * @since 2022-07-25
 */
@Api(tags = "用户信息模块")
@RestController
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private FileService fileService;

    // 获取用户角色
    @ApiOperation("通过token获取用户简单信息")
    @GetMapping("/getUserSimpInfo")
    public ResponseResult<UserSimpInfoVO> getUserSimpInfo(String token){
        UserSimpInfoVO userSimpInfo = userInfoService.getUserSimpInfo(token);
        return ResponseResult.ok(userSimpInfo);
    }

    // 获取用户角色
    @ApiOperation("获取登录用户角色信息")
    @GetMapping("/getUserRole")
    public ResponseResult<List<String>> getUserRole(String token){
        // System.out.println(token);
        List<String> roles = userInfoService.getRoles(token);
        return ResponseResult.ok(roles);
    }

    /**
     * 查询所有系统注册用户
     * @param queryInfoVO 查询条件实体类
     * @return 返回分页查询结果
     */
    @ApiOperation("分页查询系统所有用户")
    @GetMapping("/admin/getUserList")
    public ResponseResult<PageResult<UserListDTO>> getUserList(QueryInfoVO queryInfoVO){
        int pageNum = queryInfoVO.getPageNum();    // 第几页
        int pageSize = queryInfoVO.getPageSize();  // 一页多少内容
        String keywords = queryInfoVO.getKeywords();     // 查询条件
        // 分页查询用户简单信息
        List<UserListDTO> userListDTO = userInfoService.getUserList(pageNum,pageSize,keywords);
        Integer userCount = userInfoService.getUserCount(keywords);
        PageResult<UserListDTO> pageResult = new PageResult<>(userListDTO,userCount);
        return ResponseResult.ok(pageResult);
    }

    @PreAuthorize("hasAuthority('sys:admin')")
    @ApiOperation("修改用户的封禁状态")
    @PostMapping("/admin/updateUserDelete")
    public ResponseResult<String> updateUserDelete(@RequestBody UpdateUserDeleteVO updateUserDeleteVO){
        Integer userId = updateUserDeleteVO.getUserId();
        Integer isDelete = updateUserDeleteVO.getIsDelete();
        int updateRow = userAuthService.updateDelete(userId, isDelete);
        if (updateRow>0){
            return ResponseResult.ok("修改用户状态成功");
        }else {
            return ResponseResult.fail("发生错误，修改用户信息失败");
        }
    }

    @ApiOperation(value = "用户头像上传")
    @ApiImplicitParam(name = "file", value = "用户头像", required = true, dataType = "MultipartFile")
    @PostMapping("/user/avatar")
    public ResponseResult<String> headFileUpload(MultipartFile file){
        // System.out.println("成功进入方法");
        return ResponseResult.ok(fileService.avatarFileUpload(file));
    }


    @ApiOperation("修改用户个人信息")
    @PostMapping("/admin/userInfoChange")
    public ResponseResult<ChangeUserInfoVO> userInfoChange(@RequestBody ChangeUserInfoVO changeUserInfoVO){
        String nickName = changeUserInfoVO.getNickName();
        String intro = changeUserInfoVO.getIntro();
        int i = userInfoService.changeUserInfo(nickName, intro);
        if (i>0){
            return ResponseResult.ok("修改成功",null);
        }else {
            return ResponseResult.fail("修改用户信息失败");
        }
    }

    @ApiOperation("获取在线用户信息")
    @GetMapping("/admin/userInfo/online")
    public ResponseResult<PageResult<UserOnlineDTO>> getUserInfoOnline(QueryInfoVO queryInfoVO){
        return ResponseResult.ok(userInfoService.getUserOnline(queryInfoVO));
    }

    @PreAuthorize("hasAuthority('sys:admin')")
    @ApiOperation("下线用户功能")
    @PostMapping("/admin/online/{userId}/remove")
    public ResponseResult<?> removeOnlineUser(@PathVariable("userId") Integer userId){
        // 下线用户
        userInfoService.removeOnlineUser(userId);
        return ResponseResult.ok();
    }

    @ApiOperation("用户更改邮箱绑定")
    @PostMapping("/users/email")
    public ResponseResult<String> changeEmail(@RequestBody UserEmailVO userEmailVO){
        userInfoService.changeUserEmails(userEmailVO);
        return ResponseResult.ok("修改邮箱绑定成功",null);
    }

}


