package com.ming.m_blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 修改用户封禁状态
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDeleteVO {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 删除之前的isDelete标记
     */
    private Integer isDelete;

}
