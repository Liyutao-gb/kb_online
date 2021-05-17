package com.atguigu.eduservice.entity.chapter;

import lombok.Data;

/**
 * @author lytstart
 * @create 2020-08-06-11:32
 */
@Data
public class VideoVo {

    private String id;
    private String title;

    /**
     * 阿里云云端视频地址播放凭证
     */
    private String videoSourceId;
}
