package com.ming.m_blog.strategy.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ming.m_blog.constant.CommonConst;
import com.ming.m_blog.dto.search.ArticleSearchDTO;
import com.ming.m_blog.enums.ArticleStatusEnum;
import com.ming.m_blog.mapper.ArticleMapper;
import com.ming.m_blog.pojo.Article;
import com.ming.m_blog.vo.SearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 本地搜索文章
 */
@Service("mysqlArticleSearchImpl")
public class MysqlArticleSearchImpl extends AbstractSearchStrategy {


    @Autowired
    private ArticleMapper articleMapper;

    // 本地搜索文章
    @Override
    public SearchVO search(String keyword) {
        // 判空
        if (!StringUtils.hasLength(keyword)){
            return new SearchVO();
        }
        List<Article> articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, ArticleStatusEnum.PUBLIC.getStatus())
                .eq(Article::getIsDelete, CommonConst.FALSE)
                .and(a -> {
                    a.like(Article::getTitle, keyword)
                            .or()
                            .like(Article::getContent, keyword);
                })
        );
        // 将查询到的文章封装成想要的对象
        List<ArticleSearchDTO> articleSearchDTOList = articleList.stream()
                .map(article -> {
                    // 将文章查询到的关键词设置成高亮
                    String content = article.getContent();
                    String title = article.getTitle();
                    // 获取查询词出现的第一个位置
                    int index = content.indexOf(keyword);
                    if (index != -1){
                        // 获取关键词前面的文字
                        int preIndex = Math.max(index - 25, 0);
                        // 获取前面的一段文字
                        String preString = content.substring(preIndex, index);
                        // 获取包括查询条件的后面的文字
                        int keywordPostIndex = index + keyword.length();
                        // 获取关键字后面有多少长度
                        int postLength = content.length() - keywordPostIndex;
                        // 获取截取文字的最后下标
                        int postIndex = postLength > 150 ? keywordPostIndex + 150 : keywordPostIndex + postLength;
                        // 截取字符串长度
                        String postString = content.substring(preIndex, postIndex);
                        // 使文本内容高亮
                        content = (preString + postString).replaceAll(keyword,CommonConst.PRE_TAG + keyword + CommonConst.POST_TAG);
                    }
                    // 标题中含有关键字设置成高亮
                    title = title.replaceAll(keyword,CommonConst.PRE_TAG + keyword + CommonConst.POST_TAG);
                    return ArticleSearchDTO.builder()
                            .id(article.getId())
                            .title(title)
                            .content(content)
                            .build();
                }).collect(Collectors.toList());
        // 搜索文章
        return SearchVO.builder()
                .articleList(articleSearchDTOList)
                .build();
    }

}
