package com.ming.m_blog.interceptor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ming.m_blog.utils.PageUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

import static com.ming.m_blog.constant.CommonConst.*;

/**
 * 分页处理拦截器
 */
public class PageableHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String current = request.getParameter(CURRENT);
        String size = Optional.ofNullable(request.getParameter(SIZE)).orElse(DEFAULT_SIZE);
        if (!StringUtils.isEmpty(current)){
            PageUtils.setPageNum(new Page<>(Long.parseLong(current),Long.parseLong(size)));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        PageUtils.remove();
    }
}
