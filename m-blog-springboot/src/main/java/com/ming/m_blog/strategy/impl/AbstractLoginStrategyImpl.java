package com.ming.m_blog.strategy.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ming.m_blog.constant.CommonConst;
import com.ming.m_blog.dto.SocialTokenDTO;
import com.ming.m_blog.dto.SocialUserInfoDTO;
import com.ming.m_blog.dto.UserDetailDTO;
import com.ming.m_blog.dto.UserInfoDTO;
import com.ming.m_blog.enums.LoginTypeEnum;
import com.ming.m_blog.enums.RoleEnum;
import com.ming.m_blog.exception.ReRuntimeException;
import com.ming.m_blog.mapper.InUserRoleMapper;
import com.ming.m_blog.mapper.UserAuthMapper;
import com.ming.m_blog.mapper.UserInfoMapper;
import com.ming.m_blog.pojo.InUserRole;
import com.ming.m_blog.pojo.UserAuth;
import com.ming.m_blog.pojo.UserInfo;
import com.ming.m_blog.service.UserAuthService;
import com.ming.m_blog.service.UserInfoService;
import com.ming.m_blog.service.impl.UserDetailsServiceImpl;
import com.ming.m_blog.strategy.LoginStrategy;
import com.ming.m_blog.utils.BeanCopyUtils;
import com.ming.m_blog.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

/**
 * 抽象第三方登录模板
 */
public abstract class AbstractLoginStrategyImpl implements LoginStrategy {

    @Resource
    private HttpServletRequest request;
    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private InUserRoleMapper inUserRoleMapper;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * 登录
     * @param data 用户信息
     * @return
     */
    @Override
    public UserInfoDTO login(String data) {
        UserDetailDTO userDetailDTO;
        // 获取第三方的token信息
        SocialTokenDTO socialTokenDTO = getSocialTokenDTO(data);
        // 获取用户的ip信息
        String ipAddress = IpUtils.getIpAddress(request);
        String ipSource = IpUtils.getIpSource(ipAddress);
        // 判断有没有这个用户
        UserAuth userAuth = getUserAuth(socialTokenDTO);
        if (Objects.isNull(userAuth)){
            // 这个用户没有注册,将当前用户信息保存到数据库中返回
            userDetailDTO = saveUser(socialTokenDTO,ipAddress,ipSource);
        }else {
            // 当前用户已经在数据库中注册了
            userDetailDTO = getUserInfoDTO(userAuth, ipAddress, ipSource);
        }
        // 判断账号是否被禁用
        if (userDetailDTO.getIsDisable() == CommonConst.TRUE){
            throw new ReRuntimeException("账号被禁用");
        }
        // 将用户信息放入SpringSecurity
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetailDTO, null, userDetailDTO.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);


        return BeanCopyUtils.copyObject(userDetailDTO,UserInfoDTO.class);
    }

    /**
     * 获取第三方的token信息
     * @date 数据
     * @return {@link SocialTokenDTO} 第三方的token信息
     */
    public abstract SocialTokenDTO getSocialTokenDTO(String data);


    /**
     * 获取第三方登录的用户信息
     * @date 第三方token信息
     *
     */
    public abstract SocialUserInfoDTO socialUserInfoDTO(SocialTokenDTO socialTokenDTO);

    /**
     * 获取用户信息
     * @param socialTokenDTO 第三方token信息
     * @return               用户权限信息
     */
    private UserAuth getUserAuth(SocialTokenDTO socialTokenDTO){
        return userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getUsername, socialTokenDTO.getOpenId())
                .eq(UserAuth::getLoginType, socialTokenDTO.getLoginType())
        );
    }

    /**
     * 获取用户登录后返回信息
     * @param userAuth 用户信息
     * @return
     */
    private UserDetailDTO getUserInfoDTO(UserAuth userAuth, String ipAddress, String ipSource){
        UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getId, userAuth.getUserInfoId())
        );
        // 更新登录的ip信息
        userAuth.setIpAddress(ipAddress);
        userAuth.setIpSource(ipSource);
        return userDetailsService.convertUserDetail(userAuth, request);
    }

    /**
     * 保存第一次登录用户的基本信息
     * @param socialTokenDTO 第三方用户信息
     * @param ipAddress         用户登录地址
     * @param ipSource          用户ip信息
     */
    @Transactional(rollbackFor = Exception.class)
    public UserDetailDTO saveUser(SocialTokenDTO socialTokenDTO, String ipAddress, String ipSource){
        SocialUserInfoDTO socialUserInfoDTO = socialUserInfoDTO(socialTokenDTO);
        // 封装用户的基本信息存入数据库
        UserInfo userInfo = UserInfo.builder()
                .nickname(socialUserInfoDTO.getNickname())
                .avatar(socialUserInfoDTO.getAvatar())
                .build();
        userInfoMapper.insert(userInfo);
        // 设置用户基本信息的初始值(方便封装返回给前端使用)
        userInfo.setIsDelete(CommonConst.FALSE);

        // 保存账号信息
        UserAuth userAuth = UserAuth.builder()
                .userInfoId(userInfo.getId())
                .username(socialTokenDTO.getOpenId())
                .password(socialTokenDTO.getAccessToken())
                .loginType(LoginTypeEnum.QQ.getType())
                .loginTime(new Date())
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .build();
        userAuthMapper.insert(userAuth);
        // 保存用户的角色
        InUserRole inUserRole = InUserRole.builder()
                .roleId(RoleEnum.USER.getRoleId())
                .userId(userInfo.getId())
                .build();
        inUserRoleMapper.insert(inUserRole);
        return userDetailsService.convertUserDetail(userAuth,request);
    }

}
