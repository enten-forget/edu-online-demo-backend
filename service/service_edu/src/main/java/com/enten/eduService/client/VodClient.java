package com.enten.eduService.client;

import com.enten.commonutils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 在调用端创建interface,使用注解指定调用服务的名称,定义调用的方法路径
 */
@FeignClient(name = "service-vod",fallback = VodFileDegradeFeignClient.class)//指定从哪个服务中调用功能(与被调用服务名保持一致) 当熔断 调用fallback里面的方法
@Component
public interface VodClient {
    /**
     * 根据视频id删除阿里云视频
     * PathVariable注解一定要指定参数名称("videoId"),否则会出错
     */
    @DeleteMapping("/eduVod/video/removeAlyVideo/{videoId}")
    public Result removeAlyVideo(@PathVariable("videoId") String videoId);

    /**
     * 定义调用删除多个视频的方法
     */
    @DeleteMapping("/eduVod/video/delete-batch")
    public Result deleteAlyVideoBatch(@RequestParam("videoIdList") List<String> videoIdList);
}
