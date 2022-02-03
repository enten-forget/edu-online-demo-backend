package com.enten.eduCenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.enten.commonutils.JwtUtils;
import com.enten.commonutils.MD5;
import com.enten.eduCenter.entity.UcenterMember;
import com.enten.eduCenter.entity.vo.RegisterVo;
import com.enten.eduCenter.mapper.UcenterMemberMapper;
import com.enten.eduCenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enten.servicebase.exceptionhandler.EntenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author 遠天
 * @since 2021-08-27
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    // @Autowired
    // private RedisTemplate redisTemplate;


    /**
     * 登录方法
     */
    @Override
    public String login(UcenterMember member) {
        //获取登录手机号和密码
        String mobile = member.getMobile();
        String password = member.getPassword();
        //手机号和密码非空判断
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new EntenException(20001, "登录失败");
        }

        //判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);
        //判断你查询对象是否为空
        if (mobileMember == null) {//如果没有这个手机号
            throw new EntenException(20001, "未查找到用户,登录失败");
        }

        //判断密码(密码是加密的,所以要把输入的密码加密后进行比较)
        //加密方式:MD5
        if (!MD5.encrypt(password).equals(mobileMember.getPassword())) {
            throw new EntenException(20001, "密码错误,登录失败");
        }
        //判断用户是否禁用
        if (mobileMember.getIsDisabled()) {
            throw new EntenException(20001, "账号禁用,登录失败");
        }
        // 登录成功,按照规则生成token字符串,调用jwt工具栏类
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());

        return jwtToken;
    }

    /**
     * 注册方法
     */
    @Override
    public void register(RegisterVo registerVo) {
        //获取注册的数据
        String code = registerVo.getCode();//验证码
        String mobile = registerVo.getMobile();//手机号
        String nickname = registerVo.getNickname();//昵称
        String password = registerVo.getPassword();//密码

        //非空判断
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password) || StringUtils.isEmpty(code) || StringUtils.isEmpty(nickname)) {
            throw new EntenException(20001, "注册失败");
        }

        //TODO
        //判断验证码(P189)

        //判断手机号是否重复(表里存在相同手机号不添加)
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new EntenException(20001, "注册失败");
        }

        //数据添加到数据库中
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setNickname(nickname);
        member.setPassword(MD5.encrypt(password));//密码要加密
        member.setIsDisabled(false);//用户不禁用
        member.setAvatar("https://images.alphacoders.com/737/73773.jpg");
        baseMapper.insert(member);
    }

    /**
     * 判断数据库是否存在相同微信信息(根据openid判断)
     */
    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        UcenterMember member = baseMapper.selectOne(wrapper);
        return member;
    }

    /**
     * 查询某一天注册人数
     */
    @Override
    public Integer countRegister(String day) {
        return baseMapper.countRegister(day);
    }
}
