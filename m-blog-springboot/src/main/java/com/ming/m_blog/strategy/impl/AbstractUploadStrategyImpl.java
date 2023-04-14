package com.ming.m_blog.strategy.impl;

import com.ming.m_blog.strategy.UploadStrategy;
import com.ming.m_blog.utils.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 上传文件抽象模板
 */
@Service
public abstract class AbstractUploadStrategyImpl implements UploadStrategy {

    @Override
    public String uploadFile(MultipartFile multipartFile, String path) {
        // 上传文件
        try {
            // 生成md5名称
            String md5 = FileUtils.getMd5(multipartFile.getInputStream());
            // 拓展名
            String expandName = FileUtils.getFileExpand(multipartFile.getOriginalFilename());
            // 拼接新的文件名
            String fileName = md5 + expandName;
            // 判断文件是否上传过了
            if (!isExist(path + fileName)){
                // 第一次上传，直接上传文件
                upload(path,fileName,multipartFile.getInputStream());
            }
            // 返回文件访问路径
            return getFileAccessUrl(path+fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传实际操作
     * @param path      上传的相对路径
     * @param fileName  文件名
     * @param inputStream  文件输入流
     */
    abstract void upload(String path, String fileName, InputStream inputStream) throws IOException;

    /**
     * 获取文件访问路径
     * @param relativePath 相对路径
     * @return             访问路径
     */
    abstract String getFileAccessUrl(String relativePath);

    /**
     * 判断文件是否已经上传过了
     * @param fileName 文件名称
     * @return         是否已上传
     */
    abstract boolean isExist(String fileName);

}
