package com.atguigu.edusta;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author lytstart
 * @create 2020-08-13-22:03
 */
@SpringBootApplication
@ComponentScan("com.atguigu")
@EnableDiscoveryClient  //能被其他服务调用
@EnableFeignClients  //调用其他服务
@MapperScan("com.atguigu.edusta.mapper")
@EnableScheduling    //开启定时任务
public class StatisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatisticsApplication.class);
    }
}
