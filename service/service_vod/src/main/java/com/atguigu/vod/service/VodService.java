package com.atguigu.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List; /**
 * @author lytstart
 * @create 2020-08-08-8:52
 */
public interface VodService {

    //上传视频到阿里云
    String uploadVideoAliyun(MultipartFile file);

    //删除阿里云多个视频
    void removeMoreAliyunVideo(List videoIdList);
}
