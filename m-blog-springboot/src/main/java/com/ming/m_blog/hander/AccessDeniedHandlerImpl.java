package com.ming.m_blog.hander;

import com.alibaba.fastjson.JSON;
import com.ming.m_blog.constant.CommonConst;
import com.ming.m_blog.vo.ResponseResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType(CommonConst.APPLICATION_JSON);
        response.getWriter().write(JSON.toJSONString(ResponseResult.fail("权限不足")));
    }
}
