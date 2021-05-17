package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author lytstart
 * @since 2020-08-05
 */
public interface EduChapterService extends IService<EduChapter> {

    // 根据课程id查询课程大纲列表
    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    // 删除章节
    boolean deleteChapter(String chapterId);

    // 根据课程id删章节
    void removeChapterByCourseId(String courseId);
}
