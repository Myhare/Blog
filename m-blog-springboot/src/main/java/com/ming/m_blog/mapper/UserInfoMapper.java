package com.ming.m_blog.mapper;

import com.ming.m_blog.dto.UserListDTO;
import com.ming.m_blog.pojo.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
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
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    // 通过用户id查询用户角色
    List<String> selectRoleByUserId(Integer userId);

    /**
     * 后台查询用户列表
     * @param pageNum 页码
     * @param pageSize 一页的数量
     * @param keywords 查询条件
     * @return
     */
    List<UserListDTO> selectUserList(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize, @Param("keywords") String keywords);

}
