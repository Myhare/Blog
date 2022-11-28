package com.ming.m_blog.utils;

import com.alibaba.fastjson.JSON;
import com.ming.m_blog.vo.ResponseResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.ming.m_blog.constant.CommonConst.*;

public class WebUtils
{
    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string 待渲染的字符串
     * @return null
     */
    public static String renderString(HttpServletResponse response, String string) {
        try
        {
            response.setStatus(200);
            response.setContentType(APPLICATION_JSON);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将对象渲染到客户端
     *
     * @param response 渲染对象
     * @param string 待渲染的对象
     * @return null
     */
    public static String renderResult(HttpServletResponse response, ResponseResult<?> responseResult) {
        try
        {
            response.setStatus(200);
            response.setContentType(APPLICATION_JSON);
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(JSON.toJSONString(responseResult));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
