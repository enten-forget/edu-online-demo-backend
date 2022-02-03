package com.enten.eduOrder.client;

import com.enten.commonutils.ordervo.CourseWebVoOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Component
@FeignClient("service-edu")
public interface EduClient {

    /**
     * 远程调用其他服务的方法:
     * 根据课程id查询课程信息
     */
    @PostMapping("/eduService/courseFront/getCourseInfoOrder/{courseId}")
    CourseWebVoOrder getCourseInfoOrder(@PathVariable("courseId") String courseId);
}
