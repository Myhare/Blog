package com.ming.m_blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ming.m_blog.constant.RedisPrefixConst;
import com.ming.m_blog.dto.UserAreaDTO;
import com.ming.m_blog.dto.UserDetailDTO;
import com.ming.m_blog.dto.UserListDTO;
import com.ming.m_blog.dto.UserOnlineDTO;
import com.ming.m_blog.exception.ReRuntimeException;
import com.ming.m_blog.mapper.UserAuthMapper;
import com.ming.m_blog.pojo.Page;
import com.ming.m_blog.pojo.UserAuth;
import com.ming.m_blog.pojo.UserInfo;
import com.ming.m_blog.mapper.UserInfoMapper;
import com.ming.m_blog.service.RedisService;
import com.ming.m_blog.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.m_blog.utils.CommonUtils;
import com.ming.m_blog.utils.JwtUtil;
import com.ming.m_blog.utils.PageUtils;
import com.ming.m_blog.utils.UserUtils;
import com.ming.m_blog.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ming
 * @since 2022-07-25
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SessionRegistry sessionRegistry;

    // 获取用户角色
    @Override
    public List<String> getRoles(String token) {
        Integer userId;
        try {
            userId = Integer.parseInt(JwtUtil.parseJwtGetSubject(token));
        } catch (Exception e) {
            throw new ReRuntimeException("用户登录过期，请重新登录");
        }

        // 通过userId查询用户角色
        return userInfoMapper.selectRoleByUserId(userId);
    }

    // 获取用户简单信息
    @Override
    public UserSimpInfoVO getUserSimpInfo(String token) {
        Integer userId;
        try {
            userId = Integer.parseInt(JwtUtil.parseJwtGetSubject(token));
        } catch (Exception e) {
            throw new ReRuntimeException("用户登录过期，请重新登录");
        }
        // 判断token是否合法,防止重复登录和假用户
        // 不使用redis判断登录用户，鉴权完全交给SpringSecurity
        // String redisToken = (String) redisService.get(RedisPrefixConst.BACKSTAGE_LOGIN_TOKEN + userId);
        // if (!token.equals(redisToken)){
        //     throw new ReRuntimeException("非法token,请不要重复登录");
        // }

        // 通过userAuth的Id查询userInfo的id
        Integer userInfoId = userAuthMapper.selectById(userId).getUserInfoId();
        List<String> roleList = userInfoMapper.selectRoleByUserId(userInfoId);

        UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getAvatar, UserInfo::getNickname, UserInfo::getIntro)
                .eq(UserInfo::getId, userInfoId)
        );
        return UserSimpInfoVO.builder()
                .roles(roleList)
                .name(userInfo.getNickname())
                .avatar(userInfo.getAvatar())
                .introduction(userInfo.getIntro())
                .build();
    }

    // 后台分页获取用户列表
    @Override
    public List<UserListDTO> getUserList(int pageNum, int pageSize, String keywords) {
        pageNum = (pageNum-1)*pageSize;
        // 查询用户发布博客的数量
        return userInfoMapper.selectUserList(pageNum, pageSize, keywords);
    }

    // 查询一共有多少用户
    @Override
    public Integer getUserCount(String keywords) {
        return userInfoMapper.selectCount(
                new LambdaQueryWrapper<UserInfo>()
                        .like(UserInfo::getNickname, keywords));
    }

    // 修改用户简单信息
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int changeUserInfo(String nickName, String intro) {
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserId())
                .nickname(nickName)
                .intro(intro)
                .build();
        return userInfoMapper.updateById(userInfo);
    }

    // 获取当前在线用户信息
    @Override
    public PageResult<UserOnlineDTO> getUserOnline(QueryInfoVO queryInfoVO) {
        List<UserOnlineDTO> userOnlineDTOList = sessionRegistry.getAllPrincipals().stream()
                .filter(principal -> {
                    return sessionRegistry.getAllSessions(principal, false).size() > 0;
                })
                .map(principal -> JSON.parseObject(JSON.toJSONString(principal), UserOnlineDTO.class))
                .filter(item -> StringUtils.isBlank(queryInfoVO.getKeywords()) || item.getNickname().contains(queryInfoVO.getKeywords()))
                .sorted(Comparator.comparing(UserOnlineDTO::getLoginTime).reversed())
                .collect(Collectors.toList());
        // 手动执行分页操作
        int startIndex  = PageUtils.getLimitCurrent().intValue();
        int size = PageUtils.getSize().intValue();
        int endIndex = userOnlineDTOList.size() - startIndex > size ? startIndex + size : userOnlineDTOList.size();
        List<UserOnlineDTO> userOnlineDTOS = userOnlineDTOList.subList(startIndex, endIndex);
        // 获取页面的大小
        int pageSize = userOnlineDTOS.size();
        return new PageResult<>(userOnlineDTOS,pageSize);
    }

    // 下线用户功能
    @Override
    public void removeOnlineUser(Integer userInfoId) {
        List<Object> userInfoList = sessionRegistry.getAllPrincipals()
                .stream()
                .filter(item -> {
                    UserDetailDTO userDetailDTO = (UserDetailDTO) item;
                    return userDetailDTO.getUserInfoId().equals(userInfoId);
                }).collect(Collectors.toList());
        List<SessionInformation> allSessions = new ArrayList<>();
        userInfoList.forEach(userInfo -> {
            allSessions.addAll(sessionRegistry.getAllSessions(userInfo,false));
        });
        // 将这个用户的所有session注销掉
        allSessions.forEach(SessionInformation::expireNow);
    }

    // 修改用户绑定邮箱
    @Override
    public int changeUserEmails(UserEmailVO userEmailVO) {
        // 校验邮箱格式
        if (!CommonUtils.checkEmail(userEmailVO.getEmail())){
            throw new ReRuntimeException("邮箱格式错误");
        }
        // 验证码校验
        // 获取用户原始邮箱
        String redisKey = RedisPrefixConst.REGISTER_CODE + userEmailVO.getEmail();
        if (!userEmailVO.getCode().equals(redisService.get(redisKey).toString())){
            throw new ReRuntimeException("邮箱验证错误");
        }
        // 判断要修改的邮箱是否已经被绑定了
        Integer count = userInfoMapper.selectCount(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getEmail, userEmailVO.getEmail())
        );
        if (count > 0){
            throw new ReRuntimeException("邮箱已经被绑定了");
        }

        // 更新用户绑定邮箱
        Integer userInfoId = UserUtils.getLoginUserInfoId();
        UserInfo userInfo = UserInfo.builder()
                .id(userInfoId)
                .email(userEmailVO.getEmail())
                .build();
        return userInfoMapper.updateById(userInfo);
    }
}
