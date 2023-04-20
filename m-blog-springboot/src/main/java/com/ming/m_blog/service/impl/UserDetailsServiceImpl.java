package com.ming.m_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ming.m_blog.dto.UserDetailDTO;
import com.ming.m_blog.exception.ReRuntimeException;
import com.ming.m_blog.mapper.UserAuthMapper;
import com.ming.m_blog.mapper.UserInfoMapper;
import com.ming.m_blog.pojo.UserAuth;
import com.ming.m_blog.pojo.UserInfo;
import com.ming.m_blog.utils.CommonUtils;
import com.ming.m_blog.utils.IpUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.ming.m_blog.constant.RedisPrefixConst.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private RedisServiceImpl redisService;

    // 当前线程无法获取request
    // @Resource
    // private HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserInfo userInfo = null;
        UserAuth userAuth = null;
        // 判断当前username是用户名还是邮箱
        if (CommonUtils.checkEmail(username)){
            // 如果是邮箱，通过邮箱查找userInfo
            userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                    .eq(UserInfo::getEmail, username)
            );
            if (Objects.isNull(userInfo)){
                throw new ReRuntimeException("用户不存在");
            }
            // 查询UserAuth
            userAuth = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                    .eq(UserAuth::getUserInfoId, userInfo.getId())
            );
        }else {
            // 说明是用户名登录
            userAuth = userAuthMapper.selectOne(
                    new LambdaQueryWrapper<UserAuth>()
                            .eq(UserAuth::getUsername,username)
            );
            if (Objects.isNull(userAuth)){
                throw new ReRuntimeException("用户不存在");
            }
            // 通过UserAuth查询用户信息
            userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                    .eq(UserInfo::getId, userAuth.getUserInfoId())
            );
        }

        if (Objects.isNull(userAuth) || Objects.isNull(userInfo)){
            throw new ReRuntimeException("用户不存在");
        }
        return convertUserDetail(userAuth,userInfo);
    }

    /**
     * 封装用户登录信息
     * @param userAuth 用户信息
     * @return         封装结果
     */
    public UserDetailDTO convertUserDetail(UserAuth userAuth, UserInfo userInfo){
        Integer userInfoId = userAuth.getUserInfoId();

        Integer userId = userAuth.getUserInfoId();
        // 获取用户角色
        List<String> roleList = userInfoMapper.selectRoleByUserId(userId);
        // 获取权限信息
        List<String> powerList = userAuthMapper.selectPowerByUserId(Integer.toString(userId));

        // 获取用户点赞评论列表
        Set<Object> commentLikeSet = redisService.sMembers(COMMENT_USER_LIKE + userId);

        return UserDetailDTO
                .builder()
                .userId(userId)
                .userInfoId(userInfo.getId())
                .loginType(userAuth.getLoginType())
                .email(userInfo.getEmail())
                .username(userAuth.getUsername())
                .password(userAuth.getPassword())
                .nickname(userInfo.getNickname())
                .avatar(userInfo.getAvatar())
                .intro(userInfo.getIntro())
                .isDisable(userInfo.getIsDelete())
                .roleList(roleList)
                .powerList(powerList)
                .commentLikeSet(commentLikeSet) // 文章点赞集合
                .loginTime(new Date())
                .build();
    }

    /**
     * 第三方登录封装用户登录信息
     * @param userAuth 用户信息
     * @return         封装结果
     */
    public UserDetailDTO convertUserDetail(UserAuth userAuth, HttpServletRequest request){

        // 获取用户ip地址
        String ipAddress = IpUtils.getIpAddress(request);
        // 获取ip来源
        String ipSource = IpUtils.getIpSource(ipAddress);
        // 获取设备信息
        UserAgent userAgent = IpUtils.getUserAgent(request);
        String browser = userAgent.getBrowser().getName();      // 浏览器
        String os = userAgent.getOperatingSystem().getName();   // 操作系统

        UserInfo userInfo = userInfoMapper.selectById(userAuth.getUserInfoId());

        Integer userId = userAuth.getUserInfoId();
        // 获取用户角色
        List<String> roleList = userInfoMapper.selectRoleByUserId(userId);
        // 获取权限信息
        List<String> powerList = userAuthMapper.selectPowerByUserId(Integer.toString(userId));

        // 获取用户点赞评论列表
        Set<Object> commentLikeSet = redisService.sMembers(COMMENT_USER_LIKE + userId);

        return UserDetailDTO
                .builder()
                .userId(userId)
                .userInfoId(userInfo.getId())
                .loginType(userAuth.getLoginType())
                .email(userInfo.getEmail())
                .username(userAuth.getUsername())
                .password(userAuth.getPassword())
                .nickname(userInfo.getNickname())
                .avatar(userInfo.getAvatar())
                .intro(userInfo.getIntro())
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .browser(browser)
                .os(os)
                .isDisable(userInfo.getIsDelete())
                .roleList(roleList)
                .powerList(powerList)
                .commentLikeSet(commentLikeSet) // 文章点赞集合
                .loginTime(new Date())
                .build();
    }
}
