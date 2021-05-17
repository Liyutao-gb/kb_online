package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author lytstart
 * @since 2020-08-05
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    /**
     * 添加课程分类
     *
     * @param file
     * @param subjectService
     */
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService subjectService) {
        try {
            // 文件输入流(不能写绝对路径)
            InputStream in = file.getInputStream();
            // 调用方法读取
            EasyExcel.read(in, SubjectData.class, new SubjectExcelListener(subjectService)).sheet().doRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 课程分类列表(树形)
     *
     * @return
     */
    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        // 1 查询所有一级分类  parent_id=0
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", '0');
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapper);
        // this.list(wrapper)

        // 2 查询所有二级分类  parent_id!=0
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapper.ne("parent_id", '0');
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);

        // 创建最终要返回的list集合
        List<OneSubject> finalSubjectList = new ArrayList<>();

        // 3 封装一级分类
        for (int i = 0; i < oneSubjectList.size(); i++) {
            EduSubject eduSubject = oneSubjectList.get(i);

            // eduSubject里面的两个值取出来，放到OneSubject对象里面
            OneSubject oneSubject = new OneSubject();
            // oneSubject.setId(eduSubject.getId())
            // oneSubject.setTitle(eduSubject.getTitle())

            // 把数据库中的一级标题的属性值,赋值给自定义的一级标题对象
            BeanUtils.copyProperties(eduSubject, oneSubject);

            // 多个OneSubject放到finalSubjectList中去
            finalSubjectList.add(oneSubject);

            // 4 封装二级分类
            List<TwoSubject> twoFinalSubject = new ArrayList<>();
            for (int m = 0; m < twoSubjectList.size(); m++) {
                EduSubject edSubject2 = twoSubjectList.get(m);

                // 判断二级分类parent_id是否和一级分类的id是否一致
                if (edSubject2.getParentId().equals(eduSubject.getId())) {
                    // 把数据库中的二级标题的属性值,赋值给自定义的二级标题对象
                    TwoSubject twoSubject = new TwoSubject();
                    BeanUtils.copyProperties(edSubject2, twoSubject);

                    twoFinalSubject.add(twoSubject);
                }

            }

            // 把二级分类再放回一级分类
            oneSubject.setChildren(twoFinalSubject);
        }

        return finalSubjectList;
    }
}
