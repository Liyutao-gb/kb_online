package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lytstart
 * @create 2020-08-10-14:45
 */
@RestController
@RequestMapping("/eduservice/indexfront")
public class IndexFrontController {

    @Autowired
    private EduCourseService courseService;
    @Autowired
    private EduTeacherService teacherService;

    /**
     * 查询前8条热门课程，查询前4条讲师
     *
     * @return
     */
    @GetMapping("index")
    public R index() {
        List<EduCourse> courseList = courseService.getIndexCourse();
        List<EduTeacher> teacherList = teacherService.getIndexTeacher();

        return R.ok().data("courseList", courseList).data("teacherList", teacherList);
    }


}
