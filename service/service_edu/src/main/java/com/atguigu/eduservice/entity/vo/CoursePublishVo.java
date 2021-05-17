package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author lytstart
 * @create 2020-08-07-12:13
 */
@ApiModel(value = "课程发布信息")
@Data
public class CoursePublishVo {

    private String title;

    private String cover;

    private Integer lessonNum;

    private String subjectLevelOne;

    private String subjectLevelTwo;

    private String teacherName;

    /**
     * 只用于显示
     */
    private String price;
}
