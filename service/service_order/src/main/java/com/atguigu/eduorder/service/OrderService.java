package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author lytstart
 * @since 2020-08-13
 */
public interface OrderService extends IService<Order> {

    //生成订单号
    String createOrders(String courseId, String memberId);
}
