package com.ming.m_blog.hander;

import com.ming.m_blog.constant.RedisPrefixConst;
import com.ming.m_blog.service.RedisService;
import com.ming.m_blog.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登出处理类
 */
@Component
public class LogoutTokenHandlerImpl implements LogoutHandler {
    @Autowired
    private RedisService redisService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 删除redis中的token
        delLoginUserToken();
    }

    /**
     * 从redis中删除当前用户登录的token
     */
    private void delLoginUserToken(){
        try {
            Integer loginUserId = UserUtils.getLoginUserId();
            redisService.del(RedisPrefixConst.BACKSTAGE_LOGIN_TOKEN+loginUserId);
        } catch (Exception e) {
            // 手动捕获异常，不然如果服务器重启后用户登出操作会报错
            e.printStackTrace();
        }
    }
}
