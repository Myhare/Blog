package com.ming.m_blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ming.m_blog.dto.UserAreaDTO;
import com.ming.m_blog.dto.UserDetailDTO;
import com.ming.m_blog.dto.UserInfoDTO;
import com.ming.m_blog.enums.StatusCodeEnum;
import com.ming.m_blog.enums.UserTypeEnum;
import com.ming.m_blog.exception.ReRuntimeException;
import com.ming.m_blog.hander.AuthenticationSuccessHandlerImpl;
import com.ming.m_blog.mapper.UserInfoMapper;
import com.ming.m_blog.pojo.UserAuth;
import com.ming.m_blog.mapper.UserAuthMapper;
import com.ming.m_blog.pojo.UserInfo;
import com.ming.m_blog.service.RedisService;
import com.ming.m_blog.service.UserAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.m_blog.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ming.m_blog.constant.CommonConst.*;
import static com.ming.m_blog.constant.RedisPrefixConst.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ming
 * @since 2022-07-25
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements UserAuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    // 更新用户封禁状态
    @Override
    public int updateDelete(Integer userId,Integer isDelete) {
        // 获取当前登录用户id
        UserDetailDTO loginUser = UserUtils.getLoginUser();
        Integer loginUserId = loginUser.getUserId();
        if (userId.equals(loginUserId)){
            throw new ReRuntimeException("不能对自己用户的权限进行操作");
        }

        // 通过userAuth的id获取到用户信息的id
        UserAuth userAuth = userAuthMapper.selectById(userId);
        Integer userInfoId = userAuth.getUserInfoId();

        UserInfo userInfo = UserInfo.builder()
                .id(userInfoId)
                .isDelete(isDelete)
                .build();
        return userInfoMapper.updateById(userInfo);
    }

    // 查询用户位置信息
    @Override
    public List<UserAreaDTO> getUserAreaDTO(Integer type) {
        // 1用户 2游客
        List<UserAreaDTO> userAreaDTOList = new ArrayList<>();
        switch (Objects.requireNonNull(UserTypeEnum.getUserTypeEnum(type))){
            case USER:
                // 查询用户信息
                Object userAreaListObject = redisService.get(USER_AREA);
                if (!Objects.isNull(userAreaListObject)){
                    userAreaDTOList = JSON.parseObject(userAreaListObject.toString(),List.class);
                }
                break;
            case VISITOR:
                // 查询游客分布
                Map<String, Object> visitorAreaMap = redisService.hGetAll(VISITOR_AREA);
                // 封装游客分布
                userAreaDTOList = visitorAreaMap.entrySet()
                        .stream()
                        .map(item -> UserAreaDTO.builder()
                                .name(item.getKey())
                                .value(Long.valueOf(item.getValue().toString()))
                                .build()
                        ).collect(Collectors.toList());
                break;
            default:
                break;
        }
        return userAreaDTOList;
    }


    /**
     * 定时任务统计用户地区
     * 每个小时执行一次
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void statisticalUserArea(){
        // 通过地区进行分组，然后统计数量
        Map<String, Long> userAreaMap = userAuthMapper.selectList(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getIpSource)
        ).stream()
                .map(item -> {
                    if (StringUtils.isNotBlank(item.getIpSource())) {
                        return item.getIpSource()
                                .substring(0, 2)
                                .replaceAll(PROVINCE, "")
                                .replaceAll(CITY, "");
                    }
                    return UNKNOWN;
                }).collect(Collectors.groupingBy(item -> item, Collectors.counting()));
        // 将map集合转化成UserAreaDTO列表
        List<UserAreaDTO> userAreaDTOList = userAreaMap.entrySet()
                .stream()
                .map(item -> {
                    return UserAreaDTO.builder()
                            .name(item.getKey())
                            .value(item.getValue())
                            .build();
                })
                .collect(Collectors.toList());
        // 将UserAreaDTO列表存入redis中，更新信息
        redisService.set(USER_AREA, JSON.toJSONString(userAreaDTOList));
    }
}



















