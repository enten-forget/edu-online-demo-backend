package com.enten.eduCms.controller;


import com.enten.commonutils.Result;
import com.enten.eduCms.entity.CrmBanner;
import com.enten.eduCms.service.CrmBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页banner表
 *
 * @author 遠天
 * @since 2021-08-27
 */
@RestController
@RequestMapping("/eduCms/bannerFront")
// @CrossOrigin
public class BannerFrontController {
    @Autowired
    private CrmBannerService bannerService;

    /**
     * 查询所有banner
     */
    @GetMapping("getAllBanner")
    public Result getAllBanner(){
        List<CrmBanner> list = bannerService.selectAllBanner();
        return Result.ok().data("list", list);
    }
}

