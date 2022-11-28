package com.ming.m_blog.service;

import com.ming.m_blog.dto.TagListDTO;
import com.ming.m_blog.dto.TagSimpleDTO;
import com.ming.m_blog.pojo.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.m_blog.vo.PageResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Ming
 * @since 2022-08-12
 */
public interface TagService extends IService<Tag> {

    /**
     * 获取标签列表
     * @return  查询结果
     */
    PageResult<TagSimpleDTO> getTags();

    /**
     * 添加一个标签
     * @param tagName 标签名称
     * @return        数据库影响行数
     */
    int addTag(String tagName);

    /**
     * 分页查询标签信息
     * @param pageNum   想要查询的页数
     * @param pageSize  一页查询的数量
     * @param keywords  查询条件
     * @return          返回查询到的结果列表
     */
    List<TagListDTO> getTagList(int pageNum, int pageSize, String keywords);


    /**
     * 查询标签数量
     * @param keyword 查询条件
     * @return         数量
     */
    int getTagCount(String keyword);


    /**
     * 批量删除标签
     * @param tagIdList 标签列表
     * @return          返回影响行数
     */
    int delTag(List<Integer> tagIdList);

    /**
     * 查询所有标签
     * @return  查询结果
     */
    List<TagSimpleDTO> getAllTagName();

    /**
     * 模糊搜索标签
     * @param keywords 搜索条件
     * @return
     */
    List<String> getTagByKeywords(String keywords);
}
