package com.ming.m_blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.m_blog.pojo.OperationLog;
import com.ming.m_blog.service.OperationLogService;
import com.ming.m_blog.mapper.OperationLogMapper;
import org.springframework.stereotype.Service;

/**
* @author 86135
* @description 针对表【operation_log】的数据库操作Service实现
* @createDate 2023-01-13 09:08:57
*/
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog>
    implements OperationLogService{

}




