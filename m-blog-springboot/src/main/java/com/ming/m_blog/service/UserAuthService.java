package com.ming.m_blog.service;

import com.ming.m_blog.dto.user.UserAreaDTO;
import com.ming.m_blog.pojo.UserAuth;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.m_blog.vo.PasswordVO;
import com.ming.m_blog.vo.RegisterVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Ming
 * @since 2022-07-25
 */
public interface UserAuthService extends IService<UserAuth> {

    /**
     * 更新用户是否封禁
     * @param userId 想要修改封禁状态的用户id
     */
    int updateDelete(Integer userId,Integer isDelete);

    /**
     * 发送邮件
     * @param email 要发送的邮箱号
     */
    void sendEmail(String email);

    /**
     * 注册用户
     * @param registerVO 注册信息
     */
    void registerUser(RegisterVO registerVO);

    /**
     * 查询用户区域信息
     * @param type 用户类型
     * @return     查询结果
     */
    List<UserAreaDTO> getUserAreaDTO(Integer type);

    /**
     * 后台修改用户密码
     * @param passwordVO 密码对象
     * @return 影响的行数
     */
    int adminUpdatePassword(PasswordVO passwordVO);
}
