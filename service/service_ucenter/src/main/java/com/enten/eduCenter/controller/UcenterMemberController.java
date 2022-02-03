package com.enten.eduCenter.controller;


import com.enten.commonutils.JwtUtils;
import com.enten.commonutils.Result;
import com.enten.commonutils.ordervo.UcenterMemberOrder;
import com.enten.eduCenter.entity.UcenterMember;
import com.enten.eduCenter.entity.vo.RegisterVo;
import com.enten.eduCenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author 遠天
 * @since 2021-08-27
 */
@RestController
@RequestMapping("/eduCenter/member")
// @CrossOrigin
public class UcenterMemberController {
    @Autowired
    private UcenterMemberService memberService;

    /**
     * 登录方法
     */
    @PostMapping("login")
    public Result loginUser(@RequestBody UcenterMember member) {
        //member对象封装手机号和密码
        //调用service方法实现登录
        //返回一个token值,使用jwt生成
        String token = memberService.login(member);
        return Result.ok().data("token", token);
    }

    /**
     * 注册方法
     */
    @PostMapping("register")
    public Result registerUser(@RequestBody RegisterVo registerVo) {
        memberService.register(registerVo);
        return Result.ok();
    }

    /**
     * 根据token获取用户信息
     */
    @GetMapping("getMemberInfo")
    public Result getMemberInfo(HttpServletRequest request) {
        //调用jwt工具类的方法,根据request对象获取头部信息,返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库,根据用户id获取用户信息
        UcenterMember member = memberService.getById(memberId);
        return Result.ok().data("userInfo", member);
    }

    /**
     * 根据用户id获取用户信息
     */
    @PostMapping("getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id) {
        UcenterMember member = memberService.getById(id);
        UcenterMemberOrder order = new UcenterMemberOrder();
        // 把member中的值复制给UcenterMemberOrder对象
        BeanUtils.copyProperties(member, order);
        return order;
    }

    /**
     * 查询某一天注册人数
     */
    @GetMapping("countRegister/{day}")
    public Result countRegister(@PathVariable String day) {
        Integer count = memberService.countRegister(day);
        return Result.ok().data("countRegister",count);
    }
}

