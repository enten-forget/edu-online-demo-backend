package com.enten.aclService;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 权限管理服务
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.enten"})
@MapperScan("com.enten.aclService.mapper")
public class AclApplication {
    public static void main(String[] args) {
        SpringApplication.run(AclApplication.class, args);
    }

}
