package com.enten.eduService.client;

import com.enten.commonutils.Result;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 如果远程调用其他服务中的方法未能连接,调用此实现类,防止系统出错
  */
@Component
public class VodFileDegradeFeignClient implements VodClient{
    /**
     * 出错后执行
     */
    @Override
    public Result removeAlyVideo(String videoId) {
        return Result.error().message("删除视频出错");
    }

    @Override
    public Result deleteAlyVideoBatch(List<String> videoIdList) {
        return Result.error().message("删除多个视频出错");
    }
}
