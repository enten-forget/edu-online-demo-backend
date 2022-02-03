package com.enten.eduService.controller;


import com.enten.commonutils.Result;
import com.enten.eduService.entity.subject.OneSubject;
import com.enten.eduService.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author 遠天
 * @since 2021-08-19
 */
@RestController
@RequestMapping("/eduService/subject")
// @CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService subjectService;

    /**
     * 添加课程分类
     * 获取上传过来的文件,把文件内容读取出来
     */
    @PostMapping("addSubject")
    public Result addSubject(MultipartFile file) {
        //上传过来的文件
        subjectService.saveSubject(file, subjectService);
        return Result.ok();
    }
    /**
     * 课程分类列表
     */
    @GetMapping("getAllSubject")
    public Result getAllSubject(){
        List<OneSubject> list=subjectService.getAllOneTwoSubject();
        return Result.ok().data("list",list);
    }
}

