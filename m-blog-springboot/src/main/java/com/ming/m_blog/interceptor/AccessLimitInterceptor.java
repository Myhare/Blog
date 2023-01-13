package com.ming.m_blog.interceptor;

import com.ming.m_blog.constant.RedisPrefixConst;
import com.ming.m_blog.annotation.AccessLimit;
import com.ming.m_blog.service.RedisService;
import com.ming.m_blog.utils.WebUtils;
import com.ming.m_blog.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AccessLimitInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisService redisService;

    // 限制一个ip访问快速访问接口
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断请求是否属于方法的请求
        if(handler instanceof HandlerMethod){   // 如果handler是HandlerMethod的实例
            HandlerMethod hm = (HandlerMethod) handler;
            // 获取方法中的注解,看是否有该注解
            // 当前项目
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if(accessLimit != null){
                int seconds = accessLimit.seconds();  // 多少秒
                int maxCount = accessLimit.maxCount();  // 最大访问数

                //从redis中获取用户访问的次数
                String ip = request.getHeader("x-forwarded-for");      // 有可能ip是代理的
                if(ip ==null || ip.length() ==0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("Proxy-Client-IP");
                }
                if(ip ==null || ip.length() ==0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("WL-Proxy-Client-IP");
                }
                if(ip ==null || ip.length() ==0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }

                String redisKey = RedisPrefixConst.ACCESS_LIMIT + ip + ":" + hm.getMethod().getName();
                Long count = redisService.incrExpire(redisKey,seconds);
                if (count>maxCount){
                    // 说明超过了限制，向前端发送提示信息
                    WebUtils.renderResult(response, ResponseResult.fail("请求过于频繁"));
                    return false;
                }
            }
        }

        return super.preHandle(request, response, handler);  // 放行
    }

}
