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
        // 判断token是否合法
        String redisToken = (String) redisService.get(RedisPrefixConst.BACKSTAGE_LOGIN_TOKEN + userId);
        if (!token.equals(redisToken)){
            throw new ReRuntimeException("非法token");
        }

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
                    return userDetailDTO.getUserId().equals(userInfoId);
                }).collect(Collectors.toList());
        List<SessionInformation> allSessions = new ArrayList<>();
        userInfoList.forEach(userInfo -> {
            allSessions.addAll(sessionRegistry.getAllSessions(userInfo,false));
        });
        // 将这个用户的所有session注销掉
        allSessions.forEach(SessionInformation::expireNow);
    }
}
