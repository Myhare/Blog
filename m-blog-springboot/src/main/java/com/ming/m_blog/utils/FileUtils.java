package com.ming.m_blog.utils;

import com.ming.m_blog.enums.FilePathEnum;
import com.ming.m_blog.exception.ReRuntimeException;
import com.ming.m_blog.mapper.UserInfoMapper;
import com.ming.m_blog.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;

/**
 * 文件上传工具
 */
@Component
public class FileUtils {

    // 获取文件上传路径
    private static String filePath;
    // 获取文件获取的nginx映射路径
    private static String getFileUrl;

    // 通过set向静态资源注入
    @Value("${upload.path}")
    public void setFilePath(String filePath) {
        FileUtils.filePath = filePath;
    }
    @Value("${upload.url}")
    public void setGetFileUrl(String getFileUrl) {
        FileUtils.getFileUrl = getFileUrl;
    }

    /**
     * 上传文件到指定的路径
     * 具体路径
     * @param multipartFile 要上传的文件
     * @param relativePath  要上传的相对路径
     * @return              获取文件路径
     */
    public static String uploadFile(MultipartFile multipartFile,String relativePath){
        if (multipartFile.isEmpty()) {
            throw new ReRuntimeException("图片不能为空");
        }
        if (multipartFile.getSize() >= 10 * 1024 * 1024){
            throw new ReRuntimeException("图片大小超出最大限制");
        }

        // 相对路径加上图片名称
        String relativeFile;

        try {
            // 获取旧的文件名称
            String oldFileName = multipartFile.getOriginalFilename();
            // 获取文件后缀
            String suffixName = oldFileName.substring(oldFileName.lastIndexOf("."));
            // 获取新的文件名
            String newFileName =  UUIDUtils.getUUID() + suffixName;

            // 将相对路径和新文件名拼接
            relativeFile = relativePath + newFileName;

            // 拼接完整的上传路径
            String path = filePath + relativeFile;

            File dest = new File(path);
            // 如果不存在父目录，创建目录
            if (!dest.getParentFile().exists()){
                if (!dest.getParentFile().mkdirs()) {
                    throw new ReRuntimeException("创建目录失败");
                }
            }
            // 写入文件
            InputStream inputStream = multipartFile.getInputStream();

            File file = new File(path);
            if (file.createNewFile()) {
                BufferedInputStream bis = new BufferedInputStream(multipartFile.getInputStream());
                BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(file.toPath()));
                byte[] bytes = new byte[1024];
                int length;
                while ((length = bis.read(bytes)) != -1) {
                    bos.write(bytes, 0, length);
                }
                bos.flush();
                inputStream.close();
                bis.close();
                bos.close();
            }

            // multipartFile.transferTo(dest);
            // FileCopyUtils.copy(multipartFile.getInputStream(), Files.newOutputStream(dest.toPath()));  // 因为上面方法使用还不知道怎么使用绝对路径，会自动添加一个相对路径，在linux系统下会报错，这里手动通过流来赋值文件

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("上传头像出现问题");
        }

        // 拼接获取用户头像的url
        return getFileUrl + relativeFile;
    }



}
