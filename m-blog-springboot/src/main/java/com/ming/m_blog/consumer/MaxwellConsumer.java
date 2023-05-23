package com.ming.m_blog.consumer;

import com.alibaba.fastjson.JSON;
import com.ming.m_blog.constant.CommonConst;
import com.ming.m_blog.constant.MQPrefixConst;
import com.ming.m_blog.dto.MaxwellDataDTO;
import com.ming.m_blog.dto.search.TagSearchDTO;
import com.ming.m_blog.dto.search.ArticleSearchDTO;
import com.ming.m_blog.dto.search.CategorySearchDTO;
import com.ming.m_blog.mapper.elasticSearch.ArticleEsMapper;
import com.ming.m_blog.mapper.elasticSearch.CategoryEsMapper;
import com.ming.m_blog.mapper.elasticSearch.TagEsMapper;
import com.ming.m_blog.pojo.Article;
import com.ming.m_blog.pojo.Category;
import com.ming.m_blog.pojo.Tag;
import com.ming.m_blog.utils.BeanCopyUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ming.m_blog.constant.DatabaseTableConst.*;

/**
 * maxwell通知队列处理
 */
@Component
@RabbitListener(queues = MQPrefixConst.MAXWELL_QUEUE)
public class MaxwellConsumer {

    @Autowired
    private ArticleEsMapper articleEsMapper;
    @Autowired
    private CategoryEsMapper categoryEsMapper;
    @Autowired
    private TagEsMapper tagEsMapper;

    @RabbitHandler
    public void process(byte[] data){
        // 获取监听的信息
        String getJSON = new String(data);
        System.out.println(getJSON);
        MaxwellDataDTO maxwellDataDTO = JSON.parseObject(new String(data), MaxwellDataDTO.class);
        switch (maxwellDataDTO.getTable()){
            case ARTICLE:
                articleHandle(maxwellDataDTO);
                break;
            case CATEGORY:
                categoryHandle(maxwellDataDTO);
                break;
            case TAG:
                tagHandle(maxwellDataDTO);
                break;
            default:
                break;
        }
    }

    /**
     * 对article索引进行更改
     */
    private void articleHandle(MaxwellDataDTO maxwellDataDTO){
        Article article = JSON.parseObject(JSON.toJSONString(maxwellDataDTO.getData()), Article.class);
        switch (maxwellDataDTO.getType()){
            case "insert":
            case "bootstrap-insert":  // 全量更新的时候使用
            case "update":
                // 添加或修改文章
                // 判断是不是逻辑删除
                if (article.getIsDelete() == CommonConst.TRUE){
                    // 逻辑删除，这里也直接删除文档防止被搜索到
                    articleEsMapper.deleteById(article.getId());
                }else {
                    articleEsMapper.save(BeanCopyUtils.copyObject(article, ArticleSearchDTO.class));
                }
                break;
            case "delete":
                // 删除文章
                articleEsMapper.deleteById(article.getId());
                break;
            default:
                break;
        }
    }

    /**
     * 对category索引进行更改
     */
    private void categoryHandle(MaxwellDataDTO maxwellDataDTO){
        Category category = JSON.parseObject(JSON.toJSONString(maxwellDataDTO.getData()), Category.class);
        switch (maxwellDataDTO.getType()){
            case "insert":
            case "bootstrap-insert":
            case "update":
                // 添加或修改分类
                categoryEsMapper.save(BeanCopyUtils.copyObject(category, CategorySearchDTO.class));
                break;
            case "delete":
                // 删除分类
                categoryEsMapper.deleteById(category.getId());
                break;
            default:
                break;
        }
    }

    /**
     * 对tag索引进行更改
     */
    private void tagHandle(MaxwellDataDTO maxwellDataDTO){
        Tag tag = JSON.parseObject(JSON.toJSONString(maxwellDataDTO.getData()), Tag.class);
        switch (maxwellDataDTO.getType()){
            case "insert":
            case "bootstrap-insert":
            case "update":
                // 添加或修改标签
                tagEsMapper.save(BeanCopyUtils.copyObject(tag, TagSearchDTO.class));
                break;
            case "delete":
                // 删除标签
                tagEsMapper.deleteById(tag.getId());
                break;
            default:
                break;
        }
    }

}
