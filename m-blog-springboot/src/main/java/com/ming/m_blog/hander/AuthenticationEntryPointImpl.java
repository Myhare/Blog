package com.ming.m_blog.hander;

import com.alibaba.fastjson.JSON;
import com.ming.m_blog.constant.CommonConst;
import com.ming.m_blog.enums.StatusCodeEnum;
import com.ming.m_blog.vo.ResponseResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户未登录处理
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(CommonConst.APPLICATION_JSON);
        response.getWriter().write(JSON.toJSONString(ResponseResult.fail(StatusCodeEnum.NO_LOGIN.getDesc())));
    }
}
