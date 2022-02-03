package com.enten.eduOrder.service;

import com.enten.eduOrder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author 遠天
 * @since 2022-01-17
 */
public interface PayLogService extends IService<PayLog> {

    /**
     * 生成微信支付二维码接口
     */
    Map createNative(String orderId);

    /**
     * 根据订单号查询支付状态
     */
    Map<String, String> queryPayStatus(String orderId);

    /**
     * 向支付表添加距离,更新订单状态
     */
    void updateOrderStatus(Map<String, String> map);
}
