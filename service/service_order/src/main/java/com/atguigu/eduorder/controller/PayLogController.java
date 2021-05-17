package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduorder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author lytstart
 * @since 2020-08-13
 */
@RestController
@RequestMapping("/eduorder/paylog")
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    /**
     * 生成微信支付二维码(参数订单号)
     */
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo) {

        //返回信息，包括二维码地址，还有其他信息
        Map map = payLogService.createNative(orderNo);
        System.out.println("-----返回二维码map集合:"+map);

        return R.ok().data(map);
    }

    /**
     * 查询订单支付状态
     */
    @GetMapping("queryPayStatus/{orderNo}")
    public  R queryPayStatus(@PathVariable String orderNo) {

        Map<String, String> map = payLogService.queryPayStatus(orderNo);
        //每隔三秒查一次
        System.out.println("-----查询订单状态:"+map);
        if (map == null) {
            return R.error().message("支付出错了!");
        }

        //如果返回map里面不为空，通过map获取订单状态
        //NOPAY:未支付(每隔3s查询一次) SUCCESS:支付成功
        if ("SUCCESS".equals(map.get("trade_state"))) {
            //添加记录到支付表
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功~");
        }
        return R.ok().code(25000).message("支付中!");
    }


}

