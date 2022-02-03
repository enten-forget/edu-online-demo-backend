package com.enten.eduService.service;

import com.enten.eduService.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author 遠天
 * @since 2021-08-19
 */
public interface EduVideoService extends IService<EduVideo> {

    /**
     * 根据课程id删除小节
     */

    void removeVideoByCourseId(String courseId);
}
