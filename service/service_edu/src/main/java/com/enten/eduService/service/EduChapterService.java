package com.enten.eduService.service;

import com.enten.eduService.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.enten.eduService.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author 遠天
 * @since 2021-08-19
 */
public interface EduChapterService extends IService<EduChapter> {

    /**
     * 课程大纲列表,根据课程id进行查询
     */
    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    /**
     * 删除章节的方法
     */
    Boolean deleteChapter(String chapterId);

    /**
     * 根据课程id删除章节
     */
    void removeChapterByCourseId(String courseId);
}
