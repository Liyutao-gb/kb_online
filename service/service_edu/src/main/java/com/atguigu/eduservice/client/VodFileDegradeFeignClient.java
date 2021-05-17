package com.atguigu.eduservice.client;

import com.atguigu.commonutils.R;
import io.swagger.annotations.ApiModel;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lytstart
 * @create 2020-08-09-9:09
 */
@ApiModel("熔断器执行失败(宕机或超时)执行此类")
@Component
public class VodFileDegradeFeignClient implements VodClient {

    @Override
    public R removeAlyVideo(String videoId) {
        return R.error().message("删除视频出错了...");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("删除多个视频出错了...");
    }
}
