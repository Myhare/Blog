package com.ming.m_blog.service;

import com.ming.m_blog.dto.UserAreaDTO;
import com.ming.m_blog.dto.UserListDTO;
import com.ming.m_blog.dto.UserOnlineDTO;
import com.ming.m_blog.pojo.Page;
import com.ming.m_blog.pojo.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.m_blog.vo.*;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Ming
 * @since 2022-07-25
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 通过token获取用户角色信息
     * @param token 传入的token
     * @return
     */
    public List<String> getRoles(String token);

    /**
     * 通过token获取用户简单信息
     * @param token
     * @return
     */
    public UserSimpInfoVO getUserSimpInfo(String token);

    /**
     * 分页查询用户对象
     * @param pageNum 页数
     * @param pageSize 一页多少数据
     * @param keywords  搜索内容
     * @return
     */
    List<UserListDTO> getUserList(int pageNum, int pageSize, String keywords);

    /**
     * 查询一共有多少个用户
     * @param keywords 查询条件
     * @return
     */
    Integer getUserCount(String keywords);

    /**
     * 修改用户个人信息
     * @param nickName 用户昵称
     * @param intro    用户简介
     * @return         修改后的信息
     */
    int changeUserInfo(String nickName,String intro);

    /**
     * 获取当前在线用户信息
     * @return  获取结果
     */
    PageResult<UserOnlineDTO> getUserOnline(QueryInfoVO queryInfoVO);

    /**
     * 删除上线用户
     * @param userId 用户id
     */
    void removeOnlineUser(Integer userId);

    /**
     * 更改当前登录用户的邮箱
     * @param userEmailVO 邮箱类
     * @return       修改结果
     */
    int changeUserEmails(UserEmailVO userEmailVO);

}

