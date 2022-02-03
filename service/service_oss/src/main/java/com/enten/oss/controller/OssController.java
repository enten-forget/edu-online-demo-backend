package com.enten.oss.controller;

import com.enten.commonutils.Result;
import com.enten.oss.service.OssService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "阿里云文件管理")
@RestController
@RequestMapping("/eduOss/fileOss")
// @CrossOrigin
public class OssController {

    @Autowired
    private OssService ossService;

    /**
     * 上传头像的方法
     */
    @PostMapping
    public Result uploadOssFile(MultipartFile file) {
        //获取上传文件 MultipartFile
        //返回上传到oss中的路径
        String url=ossService.uploadFileAvatar(file);
        return Result.ok().data("url",url);
    }
}
