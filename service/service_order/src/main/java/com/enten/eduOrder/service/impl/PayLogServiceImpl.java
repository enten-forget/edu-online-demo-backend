package com.enten.eduOrder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enten.eduOrder.entity.Order;
import com.enten.eduOrder.entity.PayLog;
import com.enten.eduOrder.mapper.PayLogMapper;
import com.enten.eduOrder.service.OrderService;
import com.enten.eduOrder.service.PayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enten.eduOrder.utils.ConstantPropertiesUtils;
import com.enten.eduOrder.utils.HttpClient;
import com.enten.servicebase.exceptionhandler.EntenException;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付日志表 服务实现类
 *
 * @author 遠天
 * @since 2022-01-17
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    /**
     * 生成微信支付二维码接口
     */
    @Override
    public Map createNative(String orderId) {
        try {
            //1 根据订单号查询订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no", orderId);
            Order order = orderService.getOne(wrapper);

            //2 使用map设置生成二维码需要的参数
            Map m = new HashMap();
            //2.1、设置支付参数
            m.put("appid", ConstantPropertiesUtils.APP_ID);
            m.put("mch_id", ConstantPropertiesUtils.PARTNER);
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            m.put("body", order.getCourseTitle());
            m.put("out_trade_no", orderId);
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue() + "");
            m.put("spbill_create_ip", ConstantPropertiesUtils.ADDRESS_IP);
            m.put("notify_url", ConstantPropertiesUtils.NOTIFY_URL);
            m.put("trade_type", "NATIVE");

            //3 发送httpclient请求,传递参数xml格式(微信支付提供固定的地址)
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //设置XML格式参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m, ConstantPropertiesUtils.PARTNER_KEY));
            client.setHttps(true);
            //执行请求发送
            client.post();
            //4 得到发送请求返回结果(返回内容,是使用xml格式返回)
            String xml = client.getContent();
            //把xml格式转换成map集合,把集合返回
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //最终返回的数据封装
            Map map = new HashMap<>();
            map.put("out_trade_no", orderId);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", resultMap.get("result_code")); //返回二维码状态码
            map.put("code_url", resultMap.get("code_url")); // 二维码地址
            return map;
        } catch (Exception e) {
            throw new EntenException(20001, "生成二维码失败");
        }
    }

    /**
     * 根据订单号查询支付状态
     */
    @Override
    public Map<String, String> queryPayStatus(String orderId) {
        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", ConstantPropertiesUtils.APP_ID);
            m.put("mch_id", ConstantPropertiesUtils.PARTNER);
            m.put("out_trade_no", orderId);
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            //2 发送httpClient请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, ConstantPropertiesUtils.PARTNER_KEY));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            //6、转成Map
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);
            //7、返回
            return resultMap;
        } catch (Exception e) {
            throw new EntenException(20001, "查询支付状态失败");
        }
    }

    /**
     * 向支付表添加距离,更新订单状态
     */
    @Override
    public void updateOrderStatus(Map<String, String> map) {
        //从map中获取订单号
        String orderId = map.get("out_trade_no");
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderId);
        Order order = orderService.getOne(wrapper);

        // 更新订单表订单状态
        if (order.getStatus().intValue() == 1) return;
        order.setStatus(1);// 1表示已支付
        orderService.updateById(order);

        //向支付表添加支付记录
        PayLog payLog=new PayLog();
        payLog.setOrderNo(orderId);//支付订单号
        payLog.setPayTime(new Date());//完成支付时间
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(order.getTotalFee());//总金额(分)
        payLog.setTradeState(map.get("trade_state"));//支付状态
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(JSONObject.toJSONString(map));
        baseMapper.insert(payLog);
    }
}
