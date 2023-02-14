package com.ming.m_blog.service;

import com.ming.m_blog.dto.MessageBackDTO;
import com.ming.m_blog.dto.MessageDTO;
import com.ming.m_blog.pojo.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.m_blog.vo.MessageVO;
import com.ming.m_blog.vo.PageResult;
import com.ming.m_blog.vo.QueryInfoVO;
import com.ming.m_blog.vo.ReviewVO;

import java.util.List;

/**
* @author Ming
*/
public interface MessageService extends IService<Message> {

    /**
     * 添加留言
     * @param messageVO 留言信息
     */
    void saveMessage(MessageVO messageVO);

    /**
     * 查看留言列表
     * @return 查询结果
     */
    List<MessageDTO> listMessage();

    /**
     * 后台分页查询留言列表
     * @param queryInfoVO 查询条件
     * @return 查询结果
     */
    PageResult<MessageBackDTO> adminListMessage(QueryInfoVO queryInfoVO);

    /**
     * 修改留言统计
     * @param messageIdList 留言id列表
     */
    void updateMessageReview(ReviewVO reviewVO);

}
