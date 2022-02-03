package com.enten.eduCenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 用户登录服务
 */
@SpringBootApplication//取消数据源自动配置
@EnableDiscoveryClient //nacos注册
@ComponentScan({"com.enten"}) //指定扫描位置
@MapperScan("com.enten.eduCenter.mapper")
public class UcenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UcenterApplication.class, args);
    }
}