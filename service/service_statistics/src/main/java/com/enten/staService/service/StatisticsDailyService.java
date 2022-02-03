package com.enten.staService.service;

import com.enten.staService.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author 遠天
 * @since 2022-01-19
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    /**
     * 统计某一天的注册人数,生成统计数据
     */
    void registerCount(String day);

    /**
     * 图表显示,返回两部分数据,日期json数据,数量json数据
     */
    Map<String, Object> getShowData(String type, String begin, String end);
}
