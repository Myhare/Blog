package com.ming.m_blog.strategy;

import org.springframework.web.multipart.MultipartFile;

/**
 * 上传文件策略模式
 * @author liuziming
 * @date 2023-4-12
 */
public interface UploadStrategy {

    /**
     * 上传文件
     * @param multipartFile 要上传的文件
     * @param path          文件上传的相对路径
     * @return              请求文件路径
     */
    String uploadFile(MultipartFile multipartFile, String path);


}
