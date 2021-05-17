package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduTeacher;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author lytstart
 * @since 2020-02-24
 */
public interface EduTeacherService extends IService<EduTeacher> {

    /**
     * 分页查询讲师
     *
     * @param pageTeacher
     * @return
     */
    Map<String, Object> getTeacherFrontList(Page<EduTeacher> pageTeacher);

    /**
     * 查询热门讲师
     *
     * @return
     */
    List<EduTeacher> getIndexTeacher();
}
