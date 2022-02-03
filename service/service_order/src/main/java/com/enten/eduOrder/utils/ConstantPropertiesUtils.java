package com.enten.eduOrder.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantPropertiesUtils implements InitializingBean {

    @Value("${weixin.pay.appid}")
    private String appId;

    @Value("${weixin.pay.partner}")
    private String partner;

    @Value("${weixin.pay.partnerkey}")
    private String partnerKey;

    @Value("${weixin.pay.notifyurl}")
    private String notifyUrl;

    @Value("${ip.address}")
    private String addressIp;

    //定义公开静态常量
    public static String APP_ID;

    public static String PARTNER;

    public static String PARTNER_KEY;

    public static String NOTIFY_URL;

    public static String ADDRESS_IP;

    @Override
    public void afterPropertiesSet() throws Exception {
        APP_ID = appId;
        PARTNER = partner;
        PARTNER_KEY = partnerKey;
        NOTIFY_URL = notifyUrl;
        ADDRESS_IP = addressIp;
    }
}
