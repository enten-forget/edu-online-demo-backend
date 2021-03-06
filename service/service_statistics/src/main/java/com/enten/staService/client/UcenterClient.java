package com.enten.staService.client;

import com.enten.commonutils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-ucenter")
public interface UcenterClient {

    /**
     * 查询某一天注册人数
     */
    @GetMapping("/eduCenter/member/countRegister/{day}")
    Result countRegister(@PathVariable("day") String day);
}
