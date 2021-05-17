package com.atguigu.vod.controller;

import com.atguigu.vod.entity.Members;
import com.atguigu.vod.service.MembersService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Members)表控制层
 *
 * @author makejava
 * @since 2021-05-17 13:22:39
 */
@RestController
@RequestMapping("members")
public class MembersController {
    /**
     * 服务对象
     */
    @Resource
    private MembersService membersService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public Members selectOne(Integer id) {
        return this.membersService.queryById(id);
    }

}
