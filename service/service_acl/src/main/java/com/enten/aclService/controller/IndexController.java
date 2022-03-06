package com.enten.aclService.controller;

import com.alibaba.fastjson.JSONObject;
import com.enten.aclService.service.IndexService;
import com.enten.commonutils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/acl/index")
// @CrossOrigin
public class IndexController {

    @Autowired
    private IndexService indexService;

    /**
     * 根据token获取用户信息
     */
    @ApiOperation(value = "根据token获取用户信息")
    @GetMapping("info")
    public Result info(){
        //获取当前登录用户用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Object> userInfo = indexService.getUserInfo(username);
        return Result.ok().data(userInfo);
    }

    /**
     * 获取菜单
     */
    @ApiOperation(value = "获取菜单")
    @GetMapping("menu")
    public Result getMenu(){
        //获取当前登录用户用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<JSONObject> permissionList = indexService.getMenu(username);
        return Result.ok().data("permissionList", permissionList);
    }

    @ApiOperation(value = "登出")
    @PostMapping("logout")
    public Result logout(){
        return Result.ok();
    }

}
