package com.enten.eduService.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enten.commonutils.JwtUtils;
import com.enten.commonutils.Result;
import com.enten.commonutils.ordervo.CourseWebVoOrder;
import com.enten.eduService.client.OrderClient;
import com.enten.eduService.entity.EduCourse;
import com.enten.eduService.entity.EduTeacher;
import com.enten.eduService.entity.chapter.ChapterVo;
import com.enten.eduService.entity.frontVo.CourseFrontVo;
import com.enten.eduService.entity.frontVo.CourseWebVo;
import com.enten.eduService.service.EduChapterService;
import com.enten.eduService.service.EduCourseService;
import com.enten.eduService.service.EduTeacherService;
import com.enten.servicebase.exceptionhandler.EntenException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eduService/courseFront")
// @CrossOrigin
public class CourseFrontController {

    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClient orderClient;

    /**
     * 条件查询带分页查询课程
     */
    @PostMapping("getFrontCourseList/{page}/{limit}")
    public Result getFrontCourseList(@PathVariable long page, @PathVariable long limit,
                                     @RequestBody(required = false) CourseFrontVo courseFrontVo) {
        Page<EduCourse> pageCourse = new Page<>(page, limit);
        Map<String, Object> map = courseService.getCourseFrontList(pageCourse, courseFrontVo);
        //返回分页所以数据
        return Result.ok().data(map);
    }

    /**
     * 课程详情方法
     */
    @GetMapping("getFrontCourseInfo/{courseId}")
    public Result getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request) {
        //根据课程id,编写sql语句查询课程信息
        CourseWebVo courseWebVo = courseService.getBaseCourseInfo(courseId);
        //根据课程id查询章节和小节
        List<ChapterVo> chapterVideoList = chapterService.getChapterVideoByCourseId(courseId);
        //根据课程id和用户id 查询当前课程是否已经被支付过了
        if (request == null) {
            throw new EntenException(20001, "用户未登录");
        }
        Boolean isBuy = orderClient.isBuyCourse(courseId, JwtUtils.getMemberIdByJwtToken(request));

        return Result.ok().data("courseWebVo", courseWebVo).data("chapterVideoList", chapterVideoList).data("isBuy",isBuy);
    }

    /**
     * 根据课程id查询课程信息
     */
    @PostMapping("getCourseInfoOrder/{courseId}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable String courseId) {
        CourseWebVo course = courseService.getBaseCourseInfo(courseId);
        CourseWebVoOrder courseOrder = new CourseWebVoOrder();
        //// 把course中的值复制给CourseWebVoOrder对象
        BeanUtils.copyProperties(course, courseOrder);
        return courseOrder;
    }
}
