package com.atguigu.eduservice.mapper;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontvo.CourseWebVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author lytstart
 * @since 2020-08-05
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    // 查询
    CoursePublishVo getPublishCourseInfo(String courseId);

    // 根据课程id，查询课程信息
    CourseWebVo getBaseCourseInfo(String courseId);
}
