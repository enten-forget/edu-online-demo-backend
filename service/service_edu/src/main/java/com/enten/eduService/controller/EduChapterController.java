package com.enten.eduService.controller;


import com.enten.commonutils.Result;
import com.enten.eduService.entity.EduChapter;
import com.enten.eduService.entity.chapter.ChapterVo;
import com.enten.eduService.service.EduChapterService;
import com.enten.eduService.service.EduCourseService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/eduService/chapter")
// @CrossOrigin
public class EduChapterController {
    @Autowired
    private EduChapterService chapterService;

    /**
     * 课程大纲列表,根据课程id进行查询
     */
    @GetMapping("getChapterVideo/{courseId}")
    public Result getChapterVideo(@PathVariable String courseId) {
        List<ChapterVo> list = chapterService.getChapterVideoByCourseId(courseId);
        return Result.ok().data("allChapterVideo", list);
    }

    /**
     * 添加章节
     */
    @PostMapping("addChapter")
    public Result addChapter(@RequestBody EduChapter eduChapter) {
        chapterService.save(eduChapter);
        return Result.ok();
    }

    /**
     * 根据章节id查询
     */
    @GetMapping("getChapterInfo/{chapterId}")
    public Result getChapterInfo(@PathVariable String chapterId) {
        EduChapter eduChapter = chapterService.getById(chapterId);
        return Result.ok().data("chapter", eduChapter);
    }

    /**
     * 修改章节
     */
    @PostMapping("updateChapter")
    public Result updateChapter(@RequestBody EduChapter eduChapter) {
        chapterService.updateById(eduChapter);
        return Result.ok();
    }

    /**
     * 删除的方法
     */
    @DeleteMapping("{chapterId}")
    public Result deleteChapter(@PathVariable String chapterId) {
        Boolean flag = chapterService.deleteChapter(chapterId);
        return flag?Result.ok():Result.error();
    }
}

