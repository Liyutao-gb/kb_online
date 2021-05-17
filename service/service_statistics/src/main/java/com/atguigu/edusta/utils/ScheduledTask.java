package com.atguigu.edusta.utils;

import com.atguigu.edusta.service.StatisticsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author lytstart
 * @create 2020-08-14-9:15
 */
@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService dailyService;

    /**
     * 测试 cron定时器6位
     * 每隔五秒执行一次这个方法 (5s/10s/15s执行....)
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void test1(){
        System.out.println("***********task1执行了");
    }

    /**
     * 每天凌晨一点定时执行
     *  把前一天数据进行数据查询添加
     */
    @Scheduled(cron = "0 0 1 * * ?")  //cron定时器6位(第7为默认是本年)
    public void test2(){

        dailyService.registerCount(DateUtil.formatDate(DateUtil.addDays(new Date(), -1)));
    }

}
