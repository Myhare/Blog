package com.ming.m_blog.service;

import com.ming.m_blog.dto.TalkBackDTO;
import com.ming.m_blog.dto.TalkDTO;
import com.ming.m_blog.pojo.Talk;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.m_blog.vo.PageResult;
import com.ming.m_blog.vo.QueryInfoVO;
import com.ming.m_blog.vo.TalkVO;

import java.util.List;

/**
 * 说说服务操作
 * @author Ming
 * @description 针对表【talk】的数据库操作Service
 * @createDate 2023-05-18 14:40:17
 */
public interface TalkService extends IService<Talk> {

    /**
     * 保存或修改说说
     * @param talkVO 说说信息
     */
    void saveOrUpdateTalk(TalkVO talkVO);

    /**
     * 后台查询说说列表
     * @param queryInfoVO 查询信息
     * @return            查询结果
     */
    PageResult<TalkBackDTO> adminTalkList(QueryInfoVO queryInfoVO);

    /**
     * 通过id查询后台说说
     * @param talkId 说说id
     * @return       查询结果
     */
    TalkBackDTO getTalkBackById(Integer talkId);

    /**
     * 通过id删除说说
     * @param talkIdList 要删除的id列表
     * @return       影响行数
     */
    Integer delTalkByIds(List<Integer> talkIdList);

    /**
     * 查询主页说说列表
     * @return 查询结果
     */
    List<String> getHomeTalks();

    /**
     * 查询说说列表
     */
    PageResult<TalkDTO> getTalkList();

    /**
     * 通过id查询说说
     */
    TalkDTO getTalkById(Integer talkId);

    /**
     * 点赞说说
     * @param topicId 要点赞的id
     */
    void saveTalkLike(Integer topicId);
}
