package com.enten.eduService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enten.eduService.entity.EduChapter;
import com.enten.eduService.entity.EduVideo;
import com.enten.eduService.entity.chapter.ChapterVo;
import com.enten.eduService.entity.chapter.VideoVo;
import com.enten.eduService.mapper.EduChapterMapper;
import com.enten.eduService.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enten.eduService.service.EduVideoService;
import com.enten.servicebase.exceptionhandler.EntenException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author 遠天
 * @since 2021-08-19
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;//注入小节service

    /**
     * 课程大纲列表,根据课程id进行查询
     */
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {

        //1 根据课程id查询课程里面所有章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id", courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);

        //2 根据课程id查询课程里面所有的小节
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id", courseId);
        List<EduVideo> eduVideoList = videoService.list(wrapperVideo);

        //创建list集合,用于最终封装数据
        ArrayList<ChapterVo> finalList = new ArrayList<>();
        //遍历查询章节list集合进行封装
        for (int i = 0; i < eduChapterList.size(); i++) {
            //获取每一个章节
            EduChapter eduChapter = eduChapterList.get(i);
            //eduChapter对象值复制到ChapterVo里面
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter, chapterVo);

            //创建集合,用于封装章节里的小节
            ArrayList<VideoVo> videoList = new ArrayList<>();
            //遍历查询小节list集合,惊醒封装
            for (int j = 0; j < eduVideoList.size(); j++) {
                //得到每个小节
                EduVideo eduVideo = eduVideoList.get(j);
                //判断:小节里面的chapterId和章节里面的id是否一样
                if (eduVideo.getChapterId().equals(eduChapter.getId())) {
                    //进行封装
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    //放到小节封装集合
                    videoList.add(videoVo);
                }
            }
            //把封装后的小节list集合,放到章节对象里
            chapterVo.setChildren(videoList);

            //把chapterVo放到最终list集合里
            finalList.add(chapterVo);
        }


        return finalList;
    }

    /**
     * 删除章节的方法
     */
    @Override
    public Boolean deleteChapter(String chapterId) {
        //根据chapterId章节id,查询小节表,如果查询到数据,不进行删除
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", chapterId);
        int count = videoService.count(wrapper);
        if (count > 0) {//能查询出小节,不进行删除
            throw new EntenException(20001, "不能删除!");
        }else{//不能查询数据,删除
            int result = baseMapper.deleteById(chapterId);
            return result>0;
        }
    }

    /**
     * 根据课程id删除章节
     */
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        baseMapper.delete(wrapper);
    }
}
