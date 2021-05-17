package com.atguigu.educenter.controller;

/**
 * @author lytstart
 * @create 2020-08-11-20:40
 *
 * 1.第三方发起微信授权登录，用户同意授权后，会重定向到第三方网站，带上临时票据CODE
 * 2.通过CODE参数加AppID和AppSecret，通过API换取access_token(用户信息)
 * 3.通过access_token进行接口调用,获得用户信息
 *
 * 自己理解：
 *  1.跳转到扫码页面：判断商户有没有在微信开发平台注册(开发扫码登录功能)
 *  2.获取用户信息：带上第一次返回的CODE临时票据，AppID和AppSecret换取access_token(包含用户信息)
 *
 *  json转换工具：fastjson、gson、Jackson
 */

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.educenter.utils.ConstantWxUtils;
import com.atguigu.educenter.utils.HttpClientUtils;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller  //只是请求地址,不需要返回数据  @RestController
@RequestMapping("/api/ucenter/wx")
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    /**
     * 生成微信二维码的URL
     *
     * @return
     */
    @GetMapping("login")
    public String getWxCode() {
        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 回调地址，获取业务服务器重定向地址
        String redirectUrl = ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            //url编码
            redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new GuliException(20001, e.getMessage());
        }

        // 防止csrf攻击（跨站请求伪造攻击）
        //String state = UUID.randomUUID().toString().replaceAll("-", "");
        // 一般情况下会使用一个随机数
        // 为了让大家能够使用我搭建的外网的微信回调跳转服务器，这里填写你在ngrok的前置域名
        String state = "lyt";
        System.out.println("state = " + state);

        // 采用redis等进行缓存state 使用sessionId为key 30分钟后过期，可配置
        //键："wechar-open-state-" + httpServletRequest.getSession().getId()
        //值：satte
        //过期时间：30分钟

        //生成qrcodeUrl
        String qrcodeUrl = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                state);

        System.out.println(qrcodeUrl);
        return "redirect:" + qrcodeUrl;
    }

    /**
     * 获取人信息,添加数据,方法名必须是callback
     *
     * @param code
     * @param state
     * @return
     */
    @GetMapping("callback")
    public String callback(String code, String state) {
        try {
            //1.获取CODE值,就是一个临时票据,类似于验证码

            //2.拿着code请求 微信固定的地址,得到两个值 access_token和openid
            //向认证服务器发送请求换取access_token
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            //拼接参数：id 密钥 和 code值
            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code);
            //请求这个拼接好的地址,得到返回两个值 access_token和openid
            //使用 Httpclient 发送请求,得到返回结果

            //get请求
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            //{"access_token":"39_HGt7vhPtB3p7V9huP4EdcnUc29IoTHARLX3n7Uz5GhS2U1kM1NjHYLJGgVkokJjudbuxJzXmP6QsipPLNUtvdWzRKF0xuk2R1dLm45lNpCo",
            // "expires_in":7200,
            // "refresh_token":"39_mhBKE0UlAVleKXGhttSBoCCdfculNSotFVHsdlacgZa56yBLz9bibt0E-E3W6WvD76iXlw8feWtzAbqSxIqdlLBqFw7sxYJgThiTobd1-jU",
            // "openid":"o3_SC5wVAPQh5rlrkrrzuyEvsAuU",
            // "scope":"snsapi_login",
            // "unionid":"oWgGz1HIrTWbr-g7oLfMpk655vOc"}

            //从accessTokenInfo字符串取出两个值access_token和openid
            //把accessTokenInfo字符串转换map集合,根据map里面key获取对应值

            //json转换为Map集合
            Gson gson = new Gson();
            Map mapAccessToken = gson.fromJson(accessTokenInfo, HashMap.class);

            String accessToken = (String) mapAccessToken.get("access_token");
            String openid = (String) mapAccessToken.get("openid");

            //4.将信息添加到数据库
            //根据openid作为唯一性
            UcenterMember member = memberService.getOpenidMember(openid);
            if (member == null) {
                //3.拿着得到 access_token 和 openid,再去请求微信提供的固定地址,获取到扫描人信息
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //拼接两个参数
                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        accessToken,
                        openid
                );
                //发送请求,通过access_token获得用户信息
                String userInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println(userInfo);

                HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
                String nickname = (String) userInfoMap.get("nickname");
                String headimgurl = (String) userInfoMap.get("headimgurl");
                //Integer sex = (Integer) userInfoMap.get("sex")

                member = new UcenterMember();
                member.setOpenid(openid);
                member.setNickname(nickname);
                //member.setSex(sex)
                member.setAvatar(headimgurl);
                memberService.save(member);
            }

            // 生成jwt
            String token = JwtUtils.getJwtToken(member.getId(), member.getNickname());

            //存入Cookie,但是Cookie不能跨域(guli.com、atguigu.com)
            //所以token值应该放在url路径
            //CookieUtils.setCookie(request, response, "guli_jwt_token", token);

            //端口号不同存在跨域问题，cookie不能跨域，所以这里使用url重写
            return "redirect:http://localhost:3000?token=" + token;

        } catch (Exception e) {
            throw new GuliException(20001, "登录失败!");
        }

        //System.out.println(code);
        //System.out.println(state);
    }

}