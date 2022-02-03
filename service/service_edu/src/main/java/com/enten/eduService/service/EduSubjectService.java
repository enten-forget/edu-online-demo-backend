package com.enten.eduService.service;

import com.enten.eduService.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.enten.eduService.entity.subject.OneSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author 遠天
 * @since 2021-08-19
 */
public interface EduSubjectService extends IService<EduSubject> {

    /**
     * 添加课程分类
     * @param file 上传过来的文件
     */
    void saveSubject(MultipartFile file,EduSubjectService subjectService);

    List<OneSubject> getAllOneTwoSubject();
}
