package com.ming.m_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ming.m_blog.dto.talk.TagListDTO;
import com.ming.m_blog.dto.tag.TagSimpleDTO;
import com.ming.m_blog.pojo.Tag;
import com.ming.m_blog.mapper.TagMapper;
import com.ming.m_blog.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.m_blog.utils.BeanCopyUtils;
import com.ming.m_blog.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ming
 * @since 2022-08-12
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    // 获取标签列表
    @Override
    public PageResult<TagSimpleDTO> getTags() {
        List<Tag> tagList = tagMapper.selectList(null);
        List<TagSimpleDTO> tagSimpleList = BeanCopyUtils.copyList(tagList, TagSimpleDTO.class);
        Integer tagCount = tagMapper.selectCount(null);
        return new PageResult<TagSimpleDTO>(tagSimpleList, tagCount);
    }

    // 添加一个标签
    @Override
    public int addTag(String tagName) {
        Tag tag = Tag.builder()
                .tagName(tagName)
                .build();
        return tagMapper.insert(tag);
    }

    // 分页查询标签信息
    @Override
    public List<TagListDTO> getTagList(int pageNum, int pageSize, String keywords) {
        pageNum = (pageNum-1)*pageSize;
        return tagMapper.getTagList(pageNum,pageSize,keywords);
    }

    // 查询标签数量
    @Override
    public int getTagCount(String keyword) {
        return tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                .like(Tag::getTagName, keyword));
    }

    // 批量删除标签
    @Override
    public int delTag(List<Integer> tagIdList) {
        return tagMapper.deleteBatchIds(tagIdList);
    }

    // 查询所有标签结果
    @Override
    public List<TagSimpleDTO> getAllTagName() {
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId,Tag::getTagName));
        return BeanCopyUtils.copyList(tagList, TagSimpleDTO.class);
    }

    // 模糊查询标签
    @Override
    public List<String> getTagByKeywords(String keywords) {
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .like(Tag::getTagName, keywords));
        return tagList
                .stream()
                .map(Tag::getTagName)
                .collect(Collectors.toList());
    }
}
