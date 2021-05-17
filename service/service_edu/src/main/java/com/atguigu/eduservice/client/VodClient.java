package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author lytstart
 * @create 2020-08-08-17:28
 */
@Component
@FeignClient(name = "service-vod", fallback = VodFileDegradeFeignClient.class)//服务调用+熔断机制
public interface VodClient {

    // 定义调用方法路径
    // 根据视频id删除阿里云视频
    // @PathVariable注解一定要指定参数名称，否则出错
    @DeleteMapping("/eduvod/video/removeAlyVideo/{videoId}")
    R removeAlyVideo(@PathVariable("videoId") String videoId);

    // 删除多个阿里云中的视频
    // 多个视频id
    @DeleteMapping("/eduvod/video/delete-batch")
    R deleteBatch(@RequestParam("videoIdList") List<String> videoIdList);

}