package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author lytstart
 * @since 2020-08-05
 */
@RestController
@RequestMapping("/eduservice/video")
public class EduVideoController {

    @Autowired
    private EduVideoService videoService;
    @Autowired
    private VodClient vodClient;

    /**
     * 添加小节
     * @param eduVideo
     * @return
     */
    @PostMapping("addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo) {
        videoService.save(eduVideo);
        return R.ok();
    }

    /**
     * 删除小节(删除小节同时删除阿里云云端视频)
     * @param videoId
     * @return
     */
    @DeleteMapping("{videoId}")
    public R deleteVideo(@PathVariable String videoId) {

        // 1.根据小节id得到视频id
        EduVideo eduVideo = videoService.getById(videoId);
        String videoSourceId = eduVideo.getVideoSourceId();

        // 在edu中调用vod中的方法(模块之间的调用)
        if (!StringUtils.isEmpty(videoSourceId)) {
            //远程调用
            R r = vodClient.removeAlyVideo(videoSourceId);
            if (r.getCode() == 20001) {
                throw new GuliException(20001, "删除视频失败,熔断器");
            }
        }

        // 2.删除小节
        videoService.removeById(videoId);
        return R.ok();
    }

    /**
     * 根据小节id查询
     * @param videoId
     * @return
     */
    @GetMapping("getVideoInfo/{videoId}")
    public R getVideoInfo(@PathVariable String videoId) {

        EduVideo eduVideo = videoService.getById(videoId);
        return R.ok().data("video", eduVideo);
    }

    /**
     * 更新小节
     * @param eduVideo
     * @return
     */
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo) {

        videoService.updateById(eduVideo);
        return R.ok();
    }

}

