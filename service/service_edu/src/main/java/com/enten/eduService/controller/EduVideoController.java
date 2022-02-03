package com.enten.eduService.controller;


import com.enten.commonutils.Result;
import com.enten.eduService.client.VodClient;
import com.enten.eduService.entity.EduVideo;
import com.enten.eduService.service.EduVideoService;
import com.enten.servicebase.exceptionhandler.EntenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author 遠天
 * @since 2021-08-19
 */
@RestController
@RequestMapping("/eduService/video")
// @CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService videoService;

    @Autowired
    private VodClient vodClient;

    /**
     * 添加小节
     */
    @PostMapping("addVideo")
    public Result addVideo(@RequestBody EduVideo eduVideo) {
        videoService.save(eduVideo);
        return Result.ok();
    }

    /**
     * 删除小节
     * TODO
     */
    @DeleteMapping("{videoId}")
    public Result deleteVideo(@PathVariable String videoId) {
        //根据小节id获取视频id,调用方法实现视频删除
        EduVideo eduVideo = videoService.getById(videoId);
        String videoSourceId = eduVideo.getVideoSourceId();
        //判断小节里是否有视频id
        if(!StringUtils.isEmpty(videoSourceId)){
            //有,根据视频id远程调用实现视频删除
            Result result = vodClient.removeAlyVideo(videoSourceId);
            if (result.getCode() == 20001) {
                throw new EntenException(20001, "熔断,删除视频失败");
            }
        }
        boolean flag = videoService.removeById(videoId);
        return flag?Result.ok():Result.error();
    }

    /**
     * 根据小节id查询
     */
    @GetMapping("getVideoInfo/{videoId}")
    public Result getVideoInfo(@PathVariable String videoId) {
        EduVideo eduVideo = videoService.getById(videoId);
        return Result.ok().data("video", eduVideo);
    }

    /**
     * 修改小节
     */
    @PostMapping("updateVideo")
    public Result updateVideo(@RequestBody EduVideo eduVideo) {
        boolean update = videoService.updateById(eduVideo);
        return update ? Result.ok() : Result.error();
    }
}

