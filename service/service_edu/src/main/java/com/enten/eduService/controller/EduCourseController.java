package com.enten.eduService.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enten.commonutils.Result;
import com.enten.eduService.entity.EduCourse;
import com.enten.eduService.entity.EduTeacher;
import com.enten.eduService.entity.vo.CourseInfoVo;
import com.enten.eduService.entity.vo.CoursePublishVo;
import com.enten.eduService.entity.vo.CourseQuery;
import com.enten.eduService.entity.vo.TeacherQuery;
import com.enten.eduService.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author 遠天
 * @since 2021-08-19
 */
@RestController
@RequestMapping("/eduService/course")
// @CrossOrigin
public class EduCourseController {
    @Autowired
    private EduCourseService courseService;

    /**
     * 添加课程基本信息的方法
     */
    @PostMapping("addCourseInfo")
    public Result addCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        //返回添加之后的课程id,为了后面添加大纲时用
        String id = courseService.saveCourseInfo(courseInfoVo);
        return Result.ok().data("courseId", id);
    }

    /**
     * 根据课程id查询课程基本信息
     */
    @GetMapping("getCourseInfo/{courseId}")
    public Result getCourseInfo(@PathVariable String courseId) {
        CourseInfoVo courseInfoVo = courseService.getCourseInfo(courseId);
        return Result.ok().data("courseInfoVo", courseInfoVo);
    }

    /**
     * 修改课程信息
     */
    @PostMapping("updateCourseInfo")
    public Result updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo) {
        courseService.updateCourseInfo(courseInfoVo);
        return Result.ok();
    }

    /**
     * 根据课程id查询课程确认信息
     */
    @GetMapping("getPublishCourseInfo/{id}")
    public Result getPublishCourseInfo(@PathVariable String id) {
        CoursePublishVo coursePublishVo = courseService.publishCourseInfo(id);
        return Result.ok().data("coursePublish", coursePublishVo);
    }

    /**
     * 课程最终发布(修改课程状态)
     */
    @PostMapping("publishCourse/{id}")
    public Result publishCourse(@PathVariable String id) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(id);
        eduCourse.setStatus(EduCourse.COURSE_NORMAL);//设置课程发布状态
        courseService.updateById(eduCourse);
        return Result.ok();
    }

    /**
     * 分页查询课程的方法
     */
    @GetMapping("pageCourse/{current}/{limit}")
    public Result pageListCourse(@PathVariable long current, @PathVariable long limit) {
        Page<EduCourse> pageCourse = new Page<>(current, limit);
        IPage<EduCourse> page = courseService.page(pageCourse, null);
        return Result.ok().data("result", page);
    }

    /**
     * 查询课程所有数据
     */
    @GetMapping("getAllCourse")
    public Result getAllCourse() {
        List<EduCourse> list = courseService.list(null);
        return Result.ok().data("result", list);
    }

    /**
     * 条件查询带分页的讲师方法
     */
    @PostMapping("pageCourseCondition/{current}/{limit}")
    public Result pageCourseCondition(@PathVariable long current, @PathVariable long limit,
                                      @RequestBody(required = false) CourseQuery courseQuery) {
        //创建page对象
        Page<EduCourse> pageCourse = new Page<>(current, limit);
        courseService.pageQuery(pageCourse, courseQuery);
        List<EduCourse> records = pageCourse.getRecords();//数据list集合
        long total = pageCourse.getTotal();
        return Result.ok().data("total", total).data("rows", records);
    }

    /**
     * 删除课程
     */
    @DeleteMapping("{courseId}")
    public Result deleteCourse(@PathVariable String courseId) {
        courseService.removeCourse(courseId);
        return Result.ok();
    }
}

