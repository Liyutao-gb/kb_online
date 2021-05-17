package com.atguigu.edusta.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lytstart
 * @create 2020-08-13-22:19
 */
@Component
@FeignClient("service-ucenter")
public interface UcenterClient {

    //查询每日注册人数
    @GetMapping("/educenter/member/registerCount/{day}")
    public R registerCount(@PathVariable("day") String day);
}
