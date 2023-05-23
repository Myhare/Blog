package com.ming.m_blog.strategy.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ming.m_blog.constant.CommonConst;
import com.ming.m_blog.dto.search.TagSearchDTO;
import com.ming.m_blog.mapper.TagMapper;
import com.ming.m_blog.pojo.Tag;
import com.ming.m_blog.vo.SearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 本地标签搜搜
 */
@Service("mysqlTagSearchImpl")
public class MysqlTagSearchImpl extends AbstractSearchStrategy {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public SearchVO search(String keyword) {
        if (!StringUtils.hasLength(keyword)){
            return new SearchVO();
        }
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .like(Tag::getTagName, keyword)
        );
        List<TagSearchDTO> tagSearchList = tagList.stream()
                .map(tag -> {
                    String tagName = tag.getTagName();
                    tagName = tagName.replaceAll(keyword, CommonConst.PRE_TAG + keyword + CommonConst.POST_TAG);
                    return TagSearchDTO.builder()
                            .id(tag.getId())
                            .tagName(tag.getTagName())
                            .build();
                }).collect(Collectors.toList());
        return SearchVO.builder()
                .tagList(tagSearchList)
                .build();
    }
}
