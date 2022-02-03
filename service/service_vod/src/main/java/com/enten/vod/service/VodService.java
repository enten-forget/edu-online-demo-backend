package com.enten.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    /**
     * 上传视频到阿里云
     */
    String uploadVideoAly(MultipartFile file);

    /**
     * 删除多个阿里云视频的方法(参数传递多个参数)
     */
    void removeMoreAlyVideo(List videoIdList);
}
