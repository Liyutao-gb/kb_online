package com.atguigu.oss.service;

import org.springframework.web.multipart.MultipartFile;
/**
 * @author lytstart
 * @create 2020-08-04-9:50
 */
public interface OssService {

    /**
     * 上传头像文件接口
     * @param file
     * @return
     */
    String uploadFileAvatar(MultipartFile file);
}
