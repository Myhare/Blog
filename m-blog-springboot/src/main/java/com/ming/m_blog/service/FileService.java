package com.ming.m_blog.service;

import com.ming.m_blog.vo.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务接口
 */
public interface FileService {

    /**
     * 头像文件上传服务
     * @param headFile 要上传的头像文件
     * @return 上传文件后的获取路径
     */
    String avatarFileUpload(MultipartFile headFile);


    /**
     * 博客文章图片上传
     * @param articleFile 要上传的博客文章
     * @return            文章图片访问路径
     */
    String articleFileUpload(MultipartFile articleFile);

}
