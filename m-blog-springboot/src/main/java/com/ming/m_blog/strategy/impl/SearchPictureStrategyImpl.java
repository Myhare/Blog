package com.ming.m_blog.strategy.impl;

import cn.hutool.json.JSONUtil;
import com.ming.m_blog.constant.RedisPrefixConst;
import com.ming.m_blog.dto.SearchPictureDTO;
import com.ming.m_blog.exception.ReRuntimeException;
import com.ming.m_blog.service.RedisService;
import com.ming.m_blog.vo.SearchVO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * 搜索图片（爬虫）
 */
@Service("searchPictureStrategyImpl")
public class SearchPictureStrategyImpl extends AbstractSearchStrategy {

    @Autowired
    private RedisService redisService;

    @Override
    public SearchVO search(String keyword) {
        int current = 1;
        List<SearchPictureDTO> pictureList = new ArrayList<>();
        // 如果有缓存从缓存中拿数据
        String searchPicturePrefix = RedisPrefixConst.SEARCH_PICTURE + keyword;
        List<Object> pictureObjectList = redisService.lRange(searchPicturePrefix, 0, -1);
        // 如果当前有缓存，直接从缓存中获取数据
        if (pictureObjectList.size() > 0){
            pictureList = (List<SearchPictureDTO>) pictureObjectList.get(0);
            return SearchVO.builder()
                    .pictureList(pictureList)
                    .build();
        }
        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.bing.com/images/search?q="+keyword+"&first=" + current)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReRuntimeException("爬取图片失败");
        }
        // System.out.println(doc);
        Elements elements = doc.select(".iuscp.isv");
        for (Element element : elements) {
            // System.out.println(element);
            // 获取图片url
            // .attr("m")获取属性的值
            // 这里的m是一个JSON格式
            String m = element.select(".iusc").get(0).attr("m");
            Map<String,Object> map = new HashMap<>();
            map = JSONUtil.toBean(m, Map.class);
            String imageUrl = (String) map.get("turl");
            // 获取图片标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
            // System.out.println(title);
            // System.out.println(imageUrl);
            SearchPictureDTO pictureDTO = SearchPictureDTO.builder()
                    .title(title)
                    .url(imageUrl)
                    .build();
            pictureList.add(pictureDTO);
            // 获取图片的大小
            if (pictureList.size() >= 50){
                break;
            }
        }
        // 将查询到的数据存到redis中
        // 缓存一分钟
        redisService.lPush(searchPicturePrefix,pictureList,60);
        return SearchVO.builder()
                .pictureList(pictureList)
                .build();
    }
}
