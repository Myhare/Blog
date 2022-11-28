package com.ming.m_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ming.m_blog.dto.UserDetailDTO;
import com.ming.m_blog.exception.ReRuntimeException;
import com.ming.m_blog.mapper.UserAuthMapper;
import com.ming.m_blog.mapper.UserInfoMapper;
import com.ming.m_blog.pojo.UserAuth;
import com.ming.m_blog.pojo.UserInfo;
import com.ming.m_blog.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
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

    @Resource
    private HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) {

        // 通过用户名查询用户信息
        UserAuth userAuth = userAuthMapper.selectOne(
                new LambdaQueryWrapper<UserAuth>()
                        .eq(UserAuth::getUsername,username)
        );
        if (userAuth==null){
            throw new ReRuntimeException("用户不存在");
        }
        return convertUserDetail(userAuth,request);
    }

    /**
     * 封装用户登录信息
     * @param userAuth 用户信息
     * @param request  请求对象
     * @return         封装结果
     */
    public UserDetailDTO convertUserDetail(UserAuth userAuth,HttpServletRequest request){
        Integer userInfoId = userAuth.getUserInfoId();
        UserInfo userInfo = userInfoMapper.selectById(userInfoId);

        Integer userId = userAuth.getId();
        // 获取用户角色
        List<String> roleList = userInfoMapper.selectRoleByUserId(userId);
        // 获取权限信息
        List<String> powerList = userAuthMapper.selectPowerByUserId(Integer.toString(userId));

        // 获取用户点赞评论列表
        Set<Object> commentLikeSet = redisService.sMembers(COMMENT_USER_LIKE + userId);

        System.out.println(request.toString());
        // 获取用户ip地址
        String ipAddress = IpUtils.getIpAddress(request);
        // 获取ip来源
        String ipSource = IpUtils.getIpSource(ipAddress);

        return UserDetailDTO
                .builder()
                .userId(userId)
                .userInfoId(userInfoId)
                .email(userInfo.getEmail())
                .username(userAuth.getUsername())
                .password(userAuth.getPassword())
                .nickname(userInfo.getNickname())
                .avatar(userInfo.getAvatar())
                .intro(userInfo.getIntro())
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .isDisable(userInfo.getIsDelete())
                .roleList(roleList)
                .powerList(powerList)
                .commentLikeSet(commentLikeSet)
                .loginTime(new Date())
                .build();
    }
}
