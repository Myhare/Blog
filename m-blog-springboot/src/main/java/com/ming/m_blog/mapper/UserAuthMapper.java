package com.ming.m_blog.mapper;

import com.ming.m_blog.pojo.UserAuth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Ming
 * @since 2022-07-25
 */
@Repository
public interface UserAuthMapper extends BaseMapper<UserAuth> {

    /**
     * 通过用户id获取用户的权限信息
     * @param userId 用户id
     * @return 用户的权限信息
     */
    List<String> selectPowerByUserId(String userId);

}
