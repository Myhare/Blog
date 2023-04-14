package com.ming.m_blog.strategy.context;

import com.ming.m_blog.enums.UploadFileModelEnum;
import com.ming.m_blog.strategy.UploadStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 上传文件策略上下文
 * @author liuziming
 * @date 2023-4-12
 */
@Service
public class UploadFileContext {

    @Value("${upload.mode}")
    private String mode;

    @Autowired
    private Map<String, UploadStrategy> uploadStrategyMap;

    public String executeUploadStrategyMap(MultipartFile multipartFile,String relativePath){
        return uploadStrategyMap.get(UploadFileModelEnum.getStrategy(mode)).uploadFile(multipartFile,relativePath);
    }

}
