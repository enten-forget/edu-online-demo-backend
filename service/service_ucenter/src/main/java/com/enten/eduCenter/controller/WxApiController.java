package com.enten.eduCenter.controller;

import com.enten.commonutils.JwtUtils;
import com.enten.eduCenter.entity.UcenterMember;
import com.enten.eduCenter.service.UcenterMemberService;
import com.enten.eduCenter.utils.ConstantWxUtils;
import com.enten.eduCenter.utils.HttpClientUtils;
import com.enten.servicebase.exceptionhandler.EntenException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLEncoder;
import java.util.HashMap;


@Controller //只是请求地址,不需要返回数据,不能用rest
@RequestMapping("/api/ucenter/wx")
// @CrossOrigin
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;


    /**
     * 1 生成微信扫描二维码
     */
    @GetMapping("login")
    public String getWxCode() {
        //请求微信地址

        // 微信开放平台授权baseUrl, %s相当于占位符
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        String redirectUrl = ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //设置%s里面的值
        String url = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                "enten"
        );

        //用redirect重定向到请求的微信地址中去
        return "redirect:" + url;
    }

    /**
     * 获取扫描人信息,添加数据
     */
    @GetMapping("callback")
    public String callback(String code, String state) {
        try {
            //1获取到code值,临时票据,类似于验证码

            //2 拿着code请求,微信固定的地址,得到access_token和open_id
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";

            //拼接三个参数:id 密钥 code值
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code
            );
            //请求这个拼接好的地址,得到返回的两个值access_token 和 openid
            //使用httpClient发送请求,得到返回结果
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);

            //从accessTokenInfo字符串获取出来两个值 access_token 和 openId
            //把accessTokenInfo字符串转换成map集合,根据map里面key获取对应值
            //只用转换工具json
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);
            String access_token = (String) mapAccessToken.get("access_token");
            String openid = (String) mapAccessToken.get("openid");

            //把扫码人信息注入数据库里面
            //判断数据库是否存在相同微信信息(根据openid判断)
            UcenterMember member = memberService.getOpenIdMember(openid);
            if (member == null) {
                //3 通过access_token 和 openId ,再去请求 微信提供固定的地址,获取到扫描人的信息
                //访问微信的资源服务器,获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //拼接两个参数
                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        access_token,
                        openid
                );
                //发送请求
                String userInfo = HttpClientUtils.get(userInfoUrl);
                //获取返回userInfo字符串扫描人的信息
                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String) userInfoMap.get("nickname");//昵称
                String headimgurl = (String) userInfoMap.get("headimgurl");//头像
                //member为空,表中没有相同微信数据,进行添加
                member = new UcenterMember();
                member.setOpenid(openid);
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                memberService.save(member);
            }

            //使用jwt根据member对象生成token字符串
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            //最后,返回首页面
            return "redirect:http://localhost:3000?token="+jwtToken;
        } catch (Exception e) {
            throw new EntenException(20001, "登录失败");
        }
    }
}
