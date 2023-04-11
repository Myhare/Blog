package com.ming.m_blog.service;

import com.ming.m_blog.dto.OperationLogDTO;
import com.ming.m_blog.pojo.OperationLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.m_blog.vo.PageResult;
import com.ming.m_blog.vo.QueryInfoVO;

import java.util.List;

/**
* @author 86135
* @description 针对表【operation_log】的数据库操作Service
* @createDate 2023-01-13 09:08:57
*/
public interface OperationLogService extends IService<OperationLog> {

    /**
     * 后台查询日志列表
     * @param queryInfoVO 查询条件
     * @return            返回结果
     */
    public PageResult<OperationLogDTO> logList(QueryInfoVO queryInfoVO);

    /**
     * 删除日志列表
     * @param logIdList 日志id列表
     * @return          影响行数
     */
    public int delLogList(List<Integer> logIdList);
}
