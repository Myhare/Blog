package com.ming.m_blog.strategy.impl;

import com.ming.m_blog.exception.ReRuntimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;

/**
 * 本地上传文件策略
 */
@Service("localUploadStrategyImpl")
public class LocalUploadStrategyImpl extends AbstractUploadStrategyImpl{

    // 获取文件上传路径
    @Value("${upload.local.path}")
    private String localPath;
    // 获取文件获取的nginx映射路径
    @Value("${upload.local.url}")
    private String getFileUrl;


    @Override
    public void upload(String path, String fileName, InputStream inputStream) throws IOException {
        // 上传文件
        // 如果目录不存在，创建目录
        File directory = new File(localPath + path);
        if (!directory.exists()){
            if (!directory.mkdirs()) {
                throw new ReRuntimeException("创建目录失败");
            }
        }
        // 上传文件
        File file = new File(localPath + path + fileName);
        // 如果文件不存在，创建一个新文件
        if (file.createNewFile()){
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            try {
                byte[] bytes = new byte[1024];
                int length;
                while ((length = bis.read(bytes)) != -1) {
                    bos.write(bytes, 0, length);
                }
            } finally {
                inputStream.close();
                bos.close();
                bis.close();
            }

        }
    }

    // 获取文件访问路径
    @Override
    String getFileAccessUrl(String relativePath) {
        return getFileUrl + relativePath;
    }

    // 判断文件是否已上传过
    @Override
    boolean isExist(String relativePath) {
        return new File(localPath + relativePath).exists();
    }
}
