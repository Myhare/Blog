package com.ming.m_blog.mapper;

import com.ming.m_blog.dto.talk.TagListDTO;
import com.ming.m_blog.pojo.Tag;
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
 * @since 2022-08-12
 */
@Repository
public interface TagMapper extends BaseMapper<Tag> {


    /**
     * 分页查询标签信息
     * @param pageNum   页码
     * @param pageSize  每页数量
     * @param keywords  查询信息
     * @return          查询结果
     */
    List<TagListDTO> getTagList(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize, @Param("keywords") String keywords);


    /**
     * 通过文章id查询标签名称
     * @param articleId 文章id
     * @return          标签名称
     */
    List<String> getTagNameByArticleId(Integer articleId);
}
