package com.ming.m_blog.service.impl;

import com.ming.m_blog.enums.FilePathEnum;
import com.ming.m_blog.exception.ReRuntimeException;
import com.ming.m_blog.mapper.UserInfoMapper;
import com.ming.m_blog.mapper.WebsiteConfigMapper;
import com.ming.m_blog.pojo.UserInfo;
import com.ming.m_blog.pojo.WebsiteConfig;
import com.ming.m_blog.service.FileService;
import com.ming.m_blog.utils.FileUtils;
import com.ming.m_blog.utils.UUIDUtils;
import com.ming.m_blog.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传服务
 * @deprecated 现在使用策略模式上传文件，可以自己选择上传方法
 */
@Service
@Deprecated
public class FileServiceImpl implements FileService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    // 头像文件上传
    @Override
    public String avatarFileUpload(MultipartFile headFile) {
        String fileUrl = FileUtils.uploadFile(headFile, FilePathEnum.AVATAR.getPath());
        // 将更新之后获取用户头像的路径存入数据库
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserInfoId())
                .avatar(fileUrl)
                .build();
        userInfoMapper.updateById(userInfo);
        return fileUrl;
    }

    // 文章图片存储
    @Override
    public String articleFileUpload(MultipartFile articleFile) {
        return FileUtils.uploadFile(articleFile,FilePathEnum.ARTICLE.getPath());
    }

}
