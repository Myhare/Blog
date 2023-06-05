package com.ming.m_blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.m_blog.dto.log.OperationLogDTO;
import com.ming.m_blog.pojo.OperationLog;
import com.ming.m_blog.service.OperationLogService;
import com.ming.m_blog.mapper.OperationLogMapper;
import com.ming.m_blog.utils.BeanCopyUtils;
import com.ming.m_blog.utils.PageUtils;
import com.ming.m_blog.vo.PageResult;
import com.ming.m_blog.vo.QueryInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 86135
 * @description 针对表【operation_log】的数据库操作Service实现
 * @createDate 2023-01-13 09:08:57
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog>
        implements OperationLogService{

    @Autowired
    private OperationLogMapper operationLogMapper;

    // 后台查询日志列表
    @Override
    public PageResult<OperationLogDTO> logList(QueryInfoVO queryInfoVO) {
        // 根据模块名称或者操作描述进行查询
        Page<OperationLog> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        LambdaQueryWrapper<OperationLog> lambdaQueryWrapper = new LambdaQueryWrapper<OperationLog>()
                .like(StringUtils.isNotBlank(queryInfoVO.getKeywords()), OperationLog::getOptModule, queryInfoVO.getKeywords())
                .or()
                .like(StringUtils.isNotBlank(queryInfoVO.getKeywords()), OperationLog::getOptDesc, queryInfoVO.getKeywords())
                .orderByDesc(OperationLog::getCreateTime);

        Page<OperationLog> logPage = operationLogMapper.selectPage(page, lambdaQueryWrapper);
        List<OperationLog> logList = logPage.getRecords();
        List<OperationLogDTO> operationLogDTOList = BeanCopyUtils.copyList(logList, OperationLogDTO.class);
        return new PageResult<>(operationLogDTOList,(int)logPage.getTotal());
    }

    // 删除日志列表
    @Override
    public int delLogList(List<Integer> logIdList) {
        return operationLogMapper.deleteBatchIds(logIdList);
    }
}




