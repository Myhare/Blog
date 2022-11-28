package com.ming.m_blog.hander;

import com.alibaba.fastjson.JSON;
import com.ming.m_blog.service.RedisService;
import com.ming.m_blog.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ming.m_blog.constant.CommonConst.APPLICATION_JSON;

/**
 * 注销登录处理
 * @date 2022/8/2
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType(APPLICATION_JSON);
        response.getWriter().write(JSON.toJSONString(ResponseResult.ok()));
    }
}
