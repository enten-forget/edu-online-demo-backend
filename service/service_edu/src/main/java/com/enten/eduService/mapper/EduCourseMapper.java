package com.enten.eduService.mapper;

import com.enten.eduService.entity.EduCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enten.eduService.entity.frontVo.CourseWebVo;
import com.enten.eduService.entity.vo.CoursePublishVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author 遠天
 * @since 2021-08-19
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    /**
     * 根据课程id查询课程确认信息
     */
    public CoursePublishVo getPublishCourseInfo(String courseId);

    /**
     * 根据课程id,编写sql语句查询课程信息
     */
    CourseWebVo getBaseCourseInfo(String courseId);
}
