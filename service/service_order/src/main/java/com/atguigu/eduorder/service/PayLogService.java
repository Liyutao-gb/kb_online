package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author lytstart
 * @since 2020-08-13
 */
public interface PayLogService extends IService<PayLog> {

    //生成二维码
    Map createNative(String orderNo);

    //根据订单号查询订单支付状态
    Map<String,String> queryPayStatus(String orderNo);

    //向支付表中添加记录，更新支付状态
    void updateOrderStatus(Map<String, String> map);

}
