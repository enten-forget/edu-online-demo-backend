package com.enten.staService.schedule;

import com.enten.staService.service.StatisticsDailyService;
import com.enten.staService.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时任务类
 */
@Component
public class ScheduledTask {

    /*
     * cron表达式 https://cron.qqe2.com/
     * 0/5 * * * * ?: 每隔5秒执行一次
     */

    @Autowired
    private StatisticsDailyService staService;

    /**
     * 在每天凌晨1点,把前一天的数据进行生成
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void task(){
        String yesterday = DateUtil.formatDate(DateUtil.addDays(new Date(), -1));
        staService.registerCount(yesterday);
        System.out.println(yesterday+"日的数据已生成");
    }
}
