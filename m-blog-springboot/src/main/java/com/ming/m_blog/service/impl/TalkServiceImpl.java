package com.ming.m_blog.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.m_blog.constant.RedisPrefixConst;
import com.ming.m_blog.dto.comment.CommentCountDTO;
import com.ming.m_blog.dto.talk.TalkBackDTO;
import com.ming.m_blog.dto.talk.TalkDTO;
import com.ming.m_blog.enums.TalkStatusEnum;
import com.ming.m_blog.exception.ReRuntimeException;
import com.ming.m_blog.mapper.CommentMapper;
import com.ming.m_blog.pojo.Talk;
import com.ming.m_blog.service.RedisService;
import com.ming.m_blog.service.TalkService;
import com.ming.m_blog.mapper.TalkMapper;
import com.ming.m_blog.utils.*;
import com.ming.m_blog.vo.PageResult;
import com.ming.m_blog.vo.QueryInfoVO;
import com.ming.m_blog.vo.TalkVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 86135
 * @description 针对表【talk】的数据库操作Service实现
 * @createDate 2023-05-18 14:40:17
 */
@Service
public class TalkServiceImpl extends ServiceImpl<TalkMapper, Talk>
        implements TalkService{

    @Autowired
    private TalkMapper talkMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private RedisService redisService;

    // 保存或修改说说
    @Override
    public void saveOrUpdateTalk(TalkVO talkVO) {
        Talk talk = BeanCopyUtils.copyObject(talkVO, Talk.class);
        talk.setUserId(UserUtils.getLoginUserInfoId());
        this.saveOrUpdate(talk);
    }

    // 后台查询列表
    @Override
    public PageResult<TalkBackDTO> adminTalkList(QueryInfoVO queryInfoVO) {
        // Page<Talk> page = new Page<>(PageUtils.getLimitCurrent(),PageUtils.getSize());
        Integer talkCount = talkMapper.selectCount(new LambdaQueryWrapper<Talk>()
                .eq(ObjectUtil.isNotEmpty(queryInfoVO.getStatus()), Talk::getStatus, queryInfoVO.getStatus())
        );
        // 分页查询说说
        List<TalkBackDTO> talkBackList = talkMapper.talkBackList(PageUtils.getLimitCurrent(), PageUtils.getSize(), queryInfoVO.getStatus());
        return new PageResult<TalkBackDTO>(talkBackList, talkCount);
    }

    // 通过id查询后台说说
    @Override
    public TalkBackDTO getTalkBackById(Integer talkId) {
        TalkBackDTO talkBackDTO = talkMapper.getTalkBackById(talkId);
        // 将数据库中的JSON转换成string列表
        String imagesJson = talkBackDTO.getImages();
        talkBackDTO.setImgList(CommonUtils.castList(JSON.parseObject(imagesJson, List.class), String.class));
        return talkBackDTO;
    }

    // 通过id删除说说
    @Override
    public Integer delTalkByIds(List<Integer> talkIdList) {
        return talkMapper.deleteBatchIds(talkIdList);
    }

    // 查询主页说说列表
    @Override
    public List<String> getHomeTalks() {
        // 主页查询最新的10条说说
        return talkMapper.selectList(new LambdaQueryWrapper<Talk>()
                .eq(Talk::getStatus, TalkStatusEnum.PUBLIC.getStatus())
                .orderByDesc(Talk::getIsTop)
                .orderByDesc(Talk::getId)
                .last("limit 10")
        ).stream()
                .map(talk -> {
                    String content = talk.getContent();
                    // 删除html标签，防止恶意注入信息
                    return content.length() > 200 ? HTMLUtils.deleteHMTLTag(content.substring(0, 200)) : HTMLUtils.deleteHMTLTag(content);
                }).collect(Collectors.toList());
    }

    // 查询说说列表
    @Override
    public PageResult<TalkDTO> getTalkList() {
        // 查询说说总量
        Integer talkCount = talkMapper.selectCount(new LambdaQueryWrapper<Talk>()
                .eq(Talk::getStatus, TalkStatusEnum.PUBLIC.getStatus())
        );
        if (talkCount == 0){
            return new PageResult<>();
        }
        // 分页查询说说
        List<TalkDTO> talkDTOList = talkMapper.listTalks(PageUtils.getLimitCurrent(), PageUtils.getSize());
        // 查看说说评论量
        List<Integer> talkIdList = talkDTOList.stream().map(TalkDTO::getId).collect(Collectors.toList());
        Map<Integer, Integer> commentCountMap = commentMapper.listCommentCountByTopicIds(talkIdList)
                .stream()
                .collect(Collectors.toMap(CommentCountDTO::getId, CommentCountDTO::getCommentCount));
        // 查询说说点赞量
        Map<String, Object> commentLikeCount = redisService.hGetAll(RedisPrefixConst.COMMENT_LIKE_COUNT);
        // 组装
        talkDTOList.forEach(talkDTO -> {
            talkDTO.setCommentCount(commentCountMap.get(talkDTO.getId()));
            talkDTO.setLikeCount((Integer) commentLikeCount.get(talkDTO.getId().toString()));
            // 转换图片格式
            if (Objects.nonNull(talkDTO.getImages())){
                talkDTO.setImgList(CommonUtils.castList(JSON.parseObject(talkDTO.getImages(),List.class),String.class));
            }
        });
        return new PageResult<>(talkDTOList,talkCount);
    }

    // 通过id查询说说
    @Override
    public TalkDTO getTalkById(Integer talkId) {
        TalkDTO talkDTO = talkMapper.getTalkById(talkId);
        if (Objects.isNull(talkDTO)){
            throw new ReRuntimeException("说说不存在");
        }
        // 查询说说点赞量
        talkDTO.setLikeCount((Integer) redisService.hGet(RedisPrefixConst.TALK_LIKE_COUNT, talkId.toString()));
        // 转换图片格式
        if (Objects.nonNull(talkDTO.getImages())) {
            talkDTO.setImgList(CommonUtils.castList(JSON.parseObject(talkDTO.getImages(), List.class), String.class));
        }
        return talkDTO;
    }

    // 说说点赞
    @Override
    public void saveTalkLike(Integer topicId) {
        String talkLikeKey = RedisPrefixConst.TALK_USER_LIKE + UserUtils.getLoginUserInfoId();
        // 判断当前登录用户是否点赞了这个说说
        if (redisService.sIsMember(talkLikeKey,topicId)) {
            // 如果点赞了，取消点赞
            redisService.sRemove(talkLikeKey,topicId);
            // 说说点赞量减一
            redisService.hDecr(RedisPrefixConst.TALK_LIKE_COUNT, topicId.toString(),1L);
        }else {
            // 点赞
            redisService.sAdd(talkLikeKey,topicId);
            // 说说点赞量加一
            redisService.hIncr(RedisPrefixConst.TALK_LIKE_COUNT, topicId.toString(), 1L);
        }
    }
}




