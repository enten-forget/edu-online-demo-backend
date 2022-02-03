package com.enten.eduOrder.controller;


import com.enten.commonutils.Result;
import com.enten.eduOrder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author 遠天
 * @since 2022-01-17
 */
@RestController
@RequestMapping("/eduOrder/payLog")
// @CrossOrigin
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    /**
     * 生成微信支付二维码接口
     */
    @GetMapping("createNative/{orderId}")
    public Result createNative(@PathVariable String orderId) {
        //返回信息:包括二维码地址等相关信息
        Map map = payLogService.createNative(orderId);
        return Result.ok().data(map);
    }

    /**
     * 查询订单支付状态
     */
    @GetMapping("queryPayStatus/{orderId}")
    public Result queryPayStatus(@PathVariable String orderId) {
        Map<String,String> map = payLogService.queryPayStatus(orderId);
        if (map == null) {
            return Result.error().message("支付出错");
        }
        // 如果返回不为空,通过map过去订单状态
        if (map.get("trade_state").equals("SUCCESS")) {// 支付成功
            //添加记录到支付表,更新订单表订单状态
            payLogService.updateOrderStatus(map);
            return Result.ok().message("支付成功");
        }
        // 支付中,前端拦截器进行处理
        return Result.ok().code(25000).message("支付中");
    }
}

