package com.ming.m_blog.mapper;

import com.ming.m_blog.dto.talk.TalkBackDTO;
import com.ming.m_blog.dto.talk.TalkDTO;
import com.ming.m_blog.pojo.Talk;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * s
 * @author Ming
 * @description 针对表【talk】的数据库操作Mapper
 * @createDate 2023-05-18 14:40:17
 * @Entity com.ming.m_blog.pojo.Talk
 */
@Repository
public interface TalkMapper extends BaseMapper<Talk> {


    /**
     * 后台查询说说列表
     * @return            查询结果
     */
    public List<TalkBackDTO> talkBackList(@Param("current") Long current, @Param("size") Long size, @Param("status") Integer status);

    /**
     * 通过id查询后台说说
     * @param talkId 查询id
     * @return       查询结果
     */
    TalkBackDTO getTalkBackById(Integer talkId);

    /**
     * 分页查询说说
     * @param limitCurrent 页数
     * @param size         大小
     */
    List<TalkDTO> listTalks(@Param("current") Long limitCurrent,@Param("size") Long size);

    /**
     * 通过id查询说说
     * @return
     */
    TalkDTO getTalkById(@Param("talkId") Integer talkId);
}




