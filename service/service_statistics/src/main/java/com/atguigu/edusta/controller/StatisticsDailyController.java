package com.atguigu.edusta.controller;


import com.atguigu.commonutils.R;
import com.atguigu.edusta.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author lytstart
 * @since 2020-08-13
 */
@RestController
@RequestMapping("/edustatistics/sta")
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService staService;

    /**
     * 1、统计某一天注册人数(一般使用定时任务来统计数据)
     * 每天凌晨一点定时执行
     *     把前一天数据进行数据查询添加
     *  cron定时器6位(第7为默认是本年)
     */
//    @Scheduled(cron = "0 0 1 * * ?")
    @PostMapping("registerCount/{day}")
    public R registerCount(@PathVariable String day) {

        staService.registerCount(day);
        return R.ok();
    }

    /**
     * 图表显示，返回两部分数据，日期json数组，数量json数组
     *
     * @param type
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("showData/{type}/{begin}/{end}")
    public R showData(@PathVariable String type, @PathVariable String begin,
                      @PathVariable String end) {
        Map<String, Object> map = staService.getShowData(type, begin, end);
        return R.ok().data(map);
    }

}

