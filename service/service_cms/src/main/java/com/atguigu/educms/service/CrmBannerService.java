package com.atguigu.educms.service;

import com.atguigu.educms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author lytstart
 * @since 2020-08-09
 */
public interface CrmBannerService extends IService<CrmBanner> {

    //查询所有Banner
    List<CrmBanner> selectAllBanner();
}
