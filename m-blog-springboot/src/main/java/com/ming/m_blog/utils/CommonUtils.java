package com.ming.m_blog.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公共工具类
 */
public class CommonUtils {

    /**
     * 检测邮箱是否合法
     *
     * @param username 用户名
     * @return 合法状态
     */
    public static boolean checkEmail(String email) {
        if (StringUtils.isBlank(email)){
            return false;
        }
        // 正则表达式
        String rule = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
        // 邮箱的正则表达式
        return email.matches(rule);
    }

    /**
     * 获取括号内容
     *
     * @param str str
     * @return {@link String} 括号内容
     */
    public static String getBracketsContent(String str) {
        return str.substring(str.indexOf("(") + 1, str.indexOf(")"));
    }

    /**
     * 生成6位随机验证码
     *
     * @return 验证码
     */
    public static String getRandomCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }

    /**
     * 转换List
     *
     * @param obj   obj
     * @param clazz clazz
     * @return {@link List<T>}
     */
    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return result;
    }

    /**
     * 转换set
     *
     * @param obj   obj
     * @param clazz 要转化成set后储存的对象类型
     * @return {@link Set<T>}
     */
    public static <T> Set<T> castSet(Object obj, Class<T> clazz) {
        Set<T> result = new HashSet<>();
        if (obj instanceof Set<?>) {
            for (Object o : (Set<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return result;
    }


}
