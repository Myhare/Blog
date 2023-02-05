package com.ming.m_blog.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ming.m_blog.constant.CommonConst;
import com.ming.m_blog.constant.MQPrefixConst;
import com.ming.m_blog.constant.RedisPrefixConst;
import com.ming.m_blog.dto.EmailSendDTO;
import com.ming.m_blog.dto.UserAreaDTO;
import com.ming.m_blog.dto.UserDetailDTO;
import com.ming.m_blog.dto.UserInfoDTO;
import com.ming.m_blog.enums.RoleEnum;
import com.ming.m_blog.enums.StatusCodeEnum;
import com.ming.m_blog.enums.UserTypeEnum;
import com.ming.m_blog.exception.ReRuntimeException;
import com.ming.m_blog.hander.AuthenticationSuccessHandlerImpl;
import com.ming.m_blog.mapper.InUserRoleMapper;
import com.ming.m_blog.mapper.UserInfoMapper;
import com.ming.m_blog.mapper.WebsiteConfigMapper;
import com.ming.m_blog.pojo.InUserRole;
import com.ming.m_blog.pojo.UserAuth;
import com.ming.m_blog.mapper.UserAuthMapper;
import com.ming.m_blog.pojo.UserInfo;
import com.ming.m_blog.pojo.WebsiteConfig;
import com.ming.m_blog.service.BlogService;
import com.ming.m_blog.service.RedisService;
import com.ming.m_blog.service.UserAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.m_blog.service.UserInfoService;
import com.ming.m_blog.utils.CommonUtils;
import com.ming.m_blog.utils.UserUtils;
import com.ming.m_blog.vo.RegisterVO;
import com.ming.m_blog.vo.WebsiteConfigVO;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private InUserRoleMapper inUserRoleMapper;
    @Autowired
    private BlogService blogService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final int CODE_TIME = 15;

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

    // 发送邮件
    @Override
    public void sendEmail(String email) {
        // 校验格式
        if (!CommonUtils.checkEmail(email)){
            throw new ReRuntimeException("邮箱格式错误");
        }
        // 设置邮件内容
        String randomCode = CommonUtils.getRandomCode();
        String content = "邮箱验证码是" + randomCode + "有效期为"+CODE_TIME+"分钟，如果不是你发送的请无视";
        EmailSendDTO emailSendDTO = EmailSendDTO.builder()
                .email(email)
                .subject("创建账号验证码")
                .content(content)
                .build();
        // 通知rabbitMQ消费者发送邮件
        // rabbitTemplate.convertAndSend(MQPrefixConst.EMAIL_EXCHANGE,"",new Message(JSON.toJSONBytes(emailSendDTO),new MessageProperties()));
        // 直接使用对象发送,向消费者发送消息
        rabbitTemplate.convertAndSend(MQPrefixConst.EMAIL_EXCHANGE,"",emailSendDTO);
        // 将随机数存入redis
        redisService.set(REGISTER_CODE+email,randomCode,60 * CODE_TIME);
    }

    // 注册用户
    @Override
    @Transactional(rollbackFor=Exception.class)
    public void registerUser(RegisterVO registerVO) {
        // 判断邮箱合法性
        checkRegisterInfo(registerVO);
        // 获取网站信息
        WebsiteConfigVO websiteConfig = blogService.getWebsiteConfig();
        // 添加用户信息
        UserInfo userInfo = UserInfo.builder()
                .email(registerVO.getEmail())
                .nickname(DEFAULT_NICKNAME + IdWorker.getId())
                .avatar(websiteConfig.getTouristAvatar())
                .build();
        userInfoMapper.insert(userInfo);
        // 添加用户权限
        InUserRole inUserRole = InUserRole.builder()
                .roleId(RoleEnum.USER.getRoleId())
                .userId(userInfo.getId())
                .build();
        inUserRoleMapper.insert(inUserRole);
        // 添加用户账号
        UserAuth userAuth = UserAuth.builder()
                .userInfoId(userInfo.getId())   // 前面insert后id会自动填充进来
                .username(registerVO.getEmail()) // 用户名直接使用Email填充
                .password(BCrypt.hashpw(registerVO.getPassword(), BCrypt.gensalt()))
                .loginType(LOGIN_TYPE_EMAIL_CODE)   // 登录类型，这里使用邮件注册
                .build();
        userAuthMapper.insert(userAuth);
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

    /**
     * 判断邮箱合法性
     * @param registerVO 用户信息
     */
    private void checkRegisterInfo(RegisterVO registerVO) {
        String email = registerVO.getEmail();
        String code = registerVO.getCode();

        // 判断验证码是否正确
        if (!code.equals(redisService.get(REGISTER_CODE+email).toString())) {
            throw new ReRuntimeException("验证码错误");
        }
        // 判断邮箱格式
        if (!CommonUtils.checkEmail(email)) {
            throw new ReRuntimeException("邮箱格式不正确");
        }
        // 判断这个邮箱有没有注册过
        Integer count = userInfoMapper.selectCount(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getEmail, email));
        if (count>0){
            throw new ReRuntimeException("邮箱已经注册过");
        }
    }

}



















