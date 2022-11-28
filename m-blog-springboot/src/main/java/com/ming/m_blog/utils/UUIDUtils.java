package com.ming.m_blog.utils;

import java.util.UUID;

/**
 * @author wu
 * @version 1.0
 * @date 2020/11/24/024
 */
public class UUIDUtils {


    public UUIDUtils() {
    }

    /**
     * 获得一个唯一性UUID
     *
     * @return String UUID
     */
    public static String getUUID() {
        //去掉"-"
        return  UUID.randomUUID().toString().replaceAll("-","");

        //不去掉"-"
        // return  UUID.randomUUID().toString();
    }

    /**
     * 获得指定数目的UUID
     *
     * @param number int 需要获得的UUID数量
     * @return String[] UUID数组
     */
    public static String[] getUUID(int number) {
        if (number < 1) {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++) {
            ss[i] = getUUID();
        }
        return ss;
    }
    public static void main(String[] args){
        String s = getUUID();
        System.out.println(s);
    }
}
