package com.ming.m_blog.strategy.impl;

import com.ming.m_blog.config.QiniuConfigProperties;
import com.ming.m_blog.exception.ReRuntimeException;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 骑牛云上传策略
 */
@Service
public class QiNiuUploadStrategyImpl extends AbstractUploadStrategyImpl{

    @Autowired
    private QiniuConfigProperties qiniuConfigProperties;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private Auth auth;
    @Autowired
    private BucketManager bucketManager;

    // 上传文件
    @Override
    void upload(String path, String fileName, InputStream inputStream) throws IOException {
        uploadManager.put(inputStream,path+fileName,getUploadToken(),null,null);
    }

    // 获取文件访问路径
    @Override
    String getFileAccessUrl(String relativePath) {
        System.out.println(auth.privateDownloadUrl(qiniuConfigProperties.getPrefix() + relativePath));
        return auth.privateDownloadUrl(qiniuConfigProperties.getPrefix() + relativePath);
    }

    /**
     * 文件是否存在
     * @param relativePath 文件相对路径
     * @return             是否存在
     */
    @Override
    boolean isExist(String relativePath) {
        try {
            // 获取文件信息
            bucketManager.stat(qiniuConfigProperties.getBucket(),relativePath);
            return true;
        } catch (QiniuException ex) {
            if (ex.code() == 612) {
                // 文件不存在
                return false;
            } else {
                throw new ReRuntimeException("七牛云上传文件异常");
            }
        }
    }

    // 获取上传文件的token
    private String getUploadToken(){
        return auth.uploadToken(qiniuConfigProperties.getBucket());
    }

}
