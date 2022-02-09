package com.enten.staService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enten.commonutils.Result;
import com.enten.staService.client.UcenterClient;
import com.enten.staService.entity.StatisticsDaily;
import com.enten.staService.mapper.StatisticsDailyMapper;
import com.enten.staService.service.StatisticsDailyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 网站统计日数据 服务实现类
 *
 * @author 遠天
 * @since 2022-01-19
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;
    /**
     * 统计某一天的注册人数,生成统计数据
     */
    @Override
    public void registerCount(String day) {
        //删除已存在的统计对象
        QueryWrapper<StatisticsDaily> dayQueryWrapper = new QueryWrapper<>();
        dayQueryWrapper.eq("date_calculated", day);
        baseMapper.delete(dayQueryWrapper);
        //远程调用得到某一天的注册人数
        Result result = ucenterClient.countRegister(day);
        Integer countRegister = (Integer) result.getData().get("countRegister");
        //把获取到的数据添加到数据库统计分析表中
        StatisticsDaily sta = new StatisticsDaily();
        sta.setRegisterNum(countRegister);// 注册人数
        sta.setDateCalculated(day);//统计日期
        sta.setLoginNum(countRegister);//TODO
        sta.setCourseNum(countRegister);//TODO
        baseMapper.insert(sta);
    }

    /**
     * 图表显示,返回两部分数据,日期json数据,数量json数据
     */
    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
        // 根据条件查询对应的数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated", begin, end);
        wrapper.select("date_calculated", type);//选择查询出来的字段列
        List<StatisticsDaily> staList = baseMapper.selectList(wrapper);
        //因为返回有两部分数据, 日期 和对应 数量
        // 前端要求输json结构,对应后端java代码是list集合
        // 创建两个list集合,一个日期list,一个数量list
        List<String> dataCalculatedList =  new ArrayList<>();
        List<Integer> numDataList = new ArrayList<>();
        for (StatisticsDaily daily : staList) {
            dataCalculatedList.add(daily.getDateCalculated());
            switch (type) {
                case "login_num":
                    numDataList.add(daily.getLoginNum());
                    break;
                case "register_num":
                    numDataList.add(daily.getRegisterNum());
                    break;
                case "video_view_num":
                    numDataList.add(daily.getVideoViewNum());
                    break;
                case "course_num":
                    numDataList.add(daily.getCourseNum());
                    break;
                default:
                    break;
            }
        }
        // 把封装之后两个list集合放到map集合中,然后返回
        Map<String, Object> map = new HashMap<>();
        map.put("dataCalculatedList", dataCalculatedList);
        map.put("numDataList", numDataList);
        return map;
    }
}
