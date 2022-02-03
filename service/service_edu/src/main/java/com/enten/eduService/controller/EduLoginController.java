package com.enten.eduService.controller;

import com.enten.commonutils.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduService/user")
// @CrossOrigin //解决跨域问题(跨域问题:协议 ip地址 端口号有任何一个不一样)
public class EduLoginController {

    // src/store/modules/user.js

    //login
    @PostMapping("login")
    public Result login(){
        return Result.ok().data("taken","admin");
    }

    //info
    @GetMapping("info")
    public Result info(){
        return Result.ok().data("roles", "[admin]").data("name","admin")
                .data("avatar","https://giffiles.alphacoders.com/354/35422.gif");
    }

}
