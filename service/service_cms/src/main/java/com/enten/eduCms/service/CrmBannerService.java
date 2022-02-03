package com.enten.eduCms.service;

import com.enten.eduCms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author 遠天
 * @since 2021-08-27
 */
public interface CrmBannerService extends IService<CrmBanner> {

    /**
     * 查询所有banner
     */
    List<CrmBanner> selectAllBanner();
}
