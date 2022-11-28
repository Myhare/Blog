package com.ming.m_blog.service;

import com.ming.m_blog.dto.UniqueViewDTO;
import com.ming.m_blog.pojo.UniqueView;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 * 用户访问服务
 *
 * @author Ming
 * @since 2022-11-14
 */
public interface UniqueViewService extends IService<UniqueView> {

    /**
     * 获取最近七天用户访问量
     * @return 查询结果
     */
    List<UniqueViewDTO> listUniqueViewDTO();


}
