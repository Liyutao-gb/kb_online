package com.atguigu.eduorder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.entity.PayLog;
import com.atguigu.eduorder.mapper.PayLogMapper;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.service.PayLogService;
import com.atguigu.eduorder.utils.ConstantWxUtils;
import com.atguigu.eduorder.utils.HttpClient;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author lytstart
 * @since 2020-08-13
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    /**
     * 1、生成订单二维码
     *
     * @param orderNo
     * @return
     */
    @Override
    public Map createNative(String orderNo) {

        try {

            //1 根据订单号查询订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no", orderNo);
            Order order = orderService.getOne(wrapper);

            //2 使用map设置生成二维码需要参数
            Map m = new HashMap<>();
            m.put("appid", ConstantWxUtils.WX_PAY_APPID);
            m.put("mch_id", ConstantWxUtils.WX_PAY_PARTNER);
            m.put("nonce_str", WXPayUtil.generateNonceStr());
            m.put("body", order.getCourseTitle());
            m.put("out_trade_no", orderNo);
            m.put("total_fee", order.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");
            //自己开发域名地址(www.baidu.com)
            m.put("spbill_create_ip", "127.0.0.1");
            m.put("notify_url", ConstantWxUtils.WX_PAY_NOTIFYURL+"\n");
            m.put("trade_type", "NATIVE");


            //3 发送HTTPclient请求，传递参数xml格式，微信支付提供的固定地址
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");

            //client设置参数
            client.setXmlParam(WXPayUtil.generateSignedXml(m, ConstantWxUtils.WX_PAY_PARTNERKEY));
            //支持https发送
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            //4 得到发送请求返回结果
            Map map = new HashMap<>();
            map.put("out_trade_no", orderNo);
            map.put("course_id", order.getCourseId());
            map.put("total_fee", order.getTotalFee());
            map.put("result_code", resultMap.get("result_code"));
            map.put("code_url", resultMap.get("code_url"));

            //{course_id=1291647414301282305,   课程id
            // out_trade_no=20201119120610458,   订单号
            // code_url=weixin://wxpay/bizpayurl?pr=ZOkGEGZ00,  二维码固定地址
            // total_fee=0.01,  课程价格
            // result_code=SUCCESS}   成功获取二维码成功

            //微信支付二维码15分钟过期，可采取15分钟未支付取消订单
            //redisTemplate.opsForValue().set(orderNo, map, 15, TimeUnit.MINUTES);
            return map;
        } catch (Exception e) {
            throw new GuliException(20001, "二维码生成失败!");
        }

    }

    /**
     * 2、查询订单支付状态
     *
     * @param orderNo
     * @return
     */
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {
        try {
            //1、封装参数
            Map m = new HashMap<>();
            m.put("appid", ConstantWxUtils.WX_PAY_APPID);
            m.put("mch_id", ConstantWxUtils.WX_PAY_PARTNER);
            m.put("out_trade_no", orderNo);
            m.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、发送HTTPclient请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m, ConstantWxUtils.WX_PAY_PARTNERKEY));
            client.setHttps(true);
            client.post();

            //3、得到请求返回内容
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            return resultMap;
        } catch (Exception e) {
            throw new GuliException(2,"");
        }

    }

    /**
     * 3、添加支付记录和更新订单状态
     *
     * @param map
     */
    @Override
    public void updateOrderStatus(Map<String, String> map) {
        //获取订单id
        String orderNo = map.get("out_trade_no");
        //根据订单号查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderNo);
        Order order = orderService.getOne(wrapper);

        //更新订单表订单状态
        if (order.getStatus().intValue() == 1) {return; }

        //1:已支付，0:未支付
        order.setStatus(1);
        orderService.updateById(order);

        //向支付表添加SUCCESS信息
        PayLog payLog = new PayLog();
        //支付订单号
        payLog.setOrderNo(order.getOrderNo());
        payLog.setPayTime(new Date());
        //支付类型
        payLog.setPayType(1);
        //总金额(分)
        payLog.setTotalFee(order.getTotalFee());

        //支付状态   //NOPAY  SUCCESS
        payLog.setTradeState(map.get("trade_state"));
        //流水号
        payLog.setTransactionId(map.get("transaction_id"));
        payLog.setAttr(JSONObject.toJSONString(map));

        baseMapper.insert(payLog);

    }
}
