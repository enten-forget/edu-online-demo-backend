package com.enten.eduOrder.service;

import com.enten.eduOrder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author 遠天
 * @since 2022-01-17
 */
public interface OrderService extends IService<Order> {

    /**
     * 生成订单的方法
     */
    String createOrder(String courseId, String memberId);
}
