package com.ming.m_blog.controller;

import com.ming.m_blog.dto.OperationLogDTO;
import com.ming.m_blog.service.OperationLogService;
import com.ming.m_blog.vo.PageResult;
import com.ming.m_blog.vo.QueryInfoVO;
import com.ming.m_blog.vo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "日志模块")
@RestController
public class LogController {

    @Autowired
    private OperationLogService operationLogService;

    @ApiOperation("获取日志列表")
    @GetMapping("/admin/logs")
    public ResponseResult<PageResult<OperationLogDTO>> getLogList(QueryInfoVO queryInfoVO){
        return ResponseResult.ok(operationLogService.logList(queryInfoVO));
    }

    @ApiOperation("删除日志列表")
    @PreAuthorize("hasAuthority('sys:admin')")
    @DeleteMapping("/admin/logs")
    public ResponseResult<String> delLogs(@RequestBody List<Integer> logIdList){
        operationLogService.delLogList(logIdList);
        return ResponseResult.ok();
    }

}
