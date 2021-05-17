package com.atguigu.educenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author lytstart
 * @since 2020-08-11
 */
@RestController
@RequestMapping("/educenter/member")
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    /**
     * 登录(@RequestBody和PostMapping一起使用)
     *
     * @param member
     * @return
     */
    @PostMapping("login")
    public R loginUser(@RequestBody UcenterMember member) {
        //返回token值，使用jwt生成
        String token = memberService.login(member);
        return R.ok().data("token", token);
    }

    /**
     * 2.注册
     *
     * @param registerVo
     * @return
     */
    @PostMapping("register")
    public R registerUser(@RequestBody RegisterVo registerVo) {

        memberService.register(registerVo);
        return R.ok();
    }

    /**
     * 3.扫码登录后,根据token字符串获取用户信息
     *
     * @param request
     * @return
     */
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request) {
        //通过JWT工具从token值中获取用户的id值
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //获取扫码登录后的用户信息
        UcenterMember member = memberService.getById(memberId);
        return R.ok().data("userInfo", member);
    }


    /**
     * 4.订单模块：根据用户id获取用户信息
     * 返回R还需要再次取值,为了方便,直接返回一个对象
     */
    @PostMapping("getUserInfoOrder/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id) {

        UcenterMember member = memberService.getById(id);
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member, ucenterMemberOrder);

        return ucenterMemberOrder;
    }

    /**
     * 5.查询每日注册人数
     *
     * @param day
     * @return
     */
    @GetMapping("registerCount/{day}")
    public R registerCount(@PathVariable String day) {

        Integer count = memberService.registerCount(day);
        return R.ok().data("registerCount", count);
    }

}

