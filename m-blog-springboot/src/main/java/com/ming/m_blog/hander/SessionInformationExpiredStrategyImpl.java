package com.ming.m_blog.hander;

import com.alibaba.fastjson.JSON;
import com.ming.m_blog.constant.CommonConst;
import com.ming.m_blog.vo.ResponseResult;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * session被消除处理
 */
public class SessionInformationExpiredStrategyImpl implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        event.getResponse().setContentType(CommonConst.APPLICATION_JSON);
        event.getResponse().getWriter().write(JSON.toJSONString(ResponseResult.fail("Session过期，可能是被迫下线-.-")));
    }
}
