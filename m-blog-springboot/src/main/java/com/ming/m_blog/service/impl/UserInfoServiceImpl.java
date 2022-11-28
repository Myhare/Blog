package com.ming.m_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ming.m_blog.constant.RedisPrefixConst;
import com.ming.m_blog.dto.UserAreaDTO;
import com.ming.m_blog.dto.UserListDTO;
import com.ming.m_blog.exception.ReRuntimeException;
import com.ming.m_blog.pojo.UserInfo;
import com.ming.m_blog.mapper.UserInfoMapper;
import com.ming.m_blog.service.RedisService;
import com.ming.m_blog.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.m_blog.utils.JwtUtil;
import com.ming.m_blog.utils.UserUtils;
import com.ming.m_blog.vo.ChangeUserInfoVO;
import com.ming.m_blog.vo.ResponseResult;
import com.ming.m_blog.vo.UserSimpInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private UserInfoMapper userInfoMapper;
    @Autowired
    private RedisService redisService;

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

        List<String> roleList = userInfoMapper.selectRoleByUserId(userId);

        UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getAvatar, UserInfo::getNickname, UserInfo::getIntro)
                .eq(UserInfo::getId, userId)
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

}
