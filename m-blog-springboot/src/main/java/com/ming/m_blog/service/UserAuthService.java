package com.ming.m_blog.service;

import com.ming.m_blog.dto.UserAreaDTO;
import com.ming.m_blog.dto.UserInfoDTO;
import com.ming.m_blog.pojo.UserAuth;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.m_blog.pojo.UserInfo;
import com.ming.m_blog.vo.LoginUserVO;
import com.ming.m_blog.vo.RegisterVO;
import com.ming.m_blog.vo.ResponseResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

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

}
