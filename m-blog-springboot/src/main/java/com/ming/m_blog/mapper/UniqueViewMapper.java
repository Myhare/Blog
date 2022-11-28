package com.ming.m_blog.mapper;

import com.ming.m_blog.dto.UniqueViewDTO;
import com.ming.m_blog.pojo.UniqueView;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Ming
 * @since 2022-11-14
 */
@Repository
public interface UniqueViewMapper extends BaseMapper<UniqueView> {

    /**
     * 获取最近七天的访问结果
     * @return  查询结果
     */
    List<UniqueViewDTO> getUniqueViewDTO(@Param("startDate") Date startDate,@Param("endDate") Date endDate);

}
