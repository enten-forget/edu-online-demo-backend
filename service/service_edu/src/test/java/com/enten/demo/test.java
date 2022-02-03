package com.enten.demo;

import com.enten.eduService.entity.EduTeacher;
import com.enten.eduService.service.EduTeacherService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class test {

    @Autowired
    private EduTeacherService eduTeacherService;

    @Test
    public void testSelect(){
        List<EduTeacher> list = eduTeacherService.list(null);
        for (EduTeacher eduTeacher : list) {
            System.out.println(eduTeacher);
        }
    }
}
