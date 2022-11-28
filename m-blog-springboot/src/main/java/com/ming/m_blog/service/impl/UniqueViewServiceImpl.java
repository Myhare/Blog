package com.ming.m_blog.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.ming.m_blog.constant.RedisPrefixConst;
import com.ming.m_blog.dto.UniqueViewDTO;
import com.ming.m_blog.pojo.UniqueView;
import com.ming.m_blog.mapper.UniqueViewMapper;
import com.ming.m_blog.service.RedisService;
import com.ming.m_blog.service.UniqueViewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Ming
 * @since 2022-11-14
 */
@Service
public class UniqueViewServiceImpl extends ServiceImpl<UniqueViewMapper, UniqueView> implements UniqueViewService {

    @Autowired
    private UniqueViewMapper uniqueViewMapper;

    @Autowired
    private RedisService redisService;

    // 查询最近七天用户访问结果
    @Override
    public List<UniqueViewDTO> listUniqueViewDTO() {
        DateTime startDate = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -7));
        DateTime endTime = DateUtil.endOfDay(new Date());
        return uniqueViewMapper.getUniqueViewDTO(startDate,endTime);
    }

    /**
     * 定时获取用户访问量
     * 每天执行一次
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void saveUniqueView(){
        Long userCount = redisService.sSize(RedisPrefixConst.UNIQUE_VISITOR);
        // 将当天的用户访问信息存到数据库中
        // 因为是凌晨存储，所以存到昨天的数据库时间中
        UniqueView uniqueView = UniqueView.builder()
                .createTime(DateUtil.yesterday())
                .viewsCount(userCount.toString())
                .build();
        uniqueViewMapper.insert(uniqueView);
    }

    /**
     * 每天清除一次redis
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void clear(){
        // 删除游客浏览
        redisService.del(RedisPrefixConst.UNIQUE_VISITOR);
        // TODO 删除游客区域统计 (现在网站没几个人看就算了)
        // redisService.del(RedisPrefixConst.VISITOR_AREA);
    }


}
