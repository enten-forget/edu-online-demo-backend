package com.enten.eduService.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enten.eduService.entity.EduCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.enten.eduService.entity.frontVo.CourseFrontVo;
import com.enten.eduService.entity.frontVo.CourseWebVo;
import com.enten.eduService.entity.vo.CourseInfoVo;
import com.enten.eduService.entity.vo.CoursePublishVo;
import com.enten.eduService.entity.vo.CourseQuery;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author 遠天
 * @since 2021-08-19
 */
public interface EduCourseService extends IService<EduCourse> {

    /**
     * 添加课程基本信息的方法
     */
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    /**
     * 根据课程id查询课程基本信息
     */
    CourseInfoVo getCourseInfo(String courseId);

    /**
     * 修改课程信息
     */
    void updateCourseInfo(CourseInfoVo courseInfoVo);

    /**
     * 根据课程id查询课程确认信息
     */
    CoursePublishVo publishCourseInfo(String courseId);

    /**
     * 条件查询带分页的讲师方法
     */
    void pageQuery(Page<EduCourse> pageCourse, CourseQuery courseQuery);

    /**
     * 删除课程
     */
    void removeCourse(String courseId);

    /**
     * 条件查询带分页查询课程
     */
    Map<String, Object> getCourseFrontList(Page<EduCourse> pageCourse, CourseFrontVo courseFrontVo);

    /**
     * 根据课程id,编写sql语句查询课程信息
     */
    CourseWebVo getBaseCourseInfo(String courseId);
}
