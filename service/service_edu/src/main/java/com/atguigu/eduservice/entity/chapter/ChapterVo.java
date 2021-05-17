package com.atguigu.eduservice.entity.chapter;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lytstart
 * @create 2020-08-06-11:32
 */
@Data
public class ChapterVo {

    private String id;
    private String title;

    /**
     * 包含小结
     */
    private List<VideoVo> children = new ArrayList<>();
}
