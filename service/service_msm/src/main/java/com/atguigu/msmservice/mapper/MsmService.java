package com.atguigu.msmservice.mapper;

import java.util.Map; /**
 * @author lytstart
 * @create 2020-08-11-8:44
 */
public interface MsmService {

    /**
     * 发送验证码
     * @param param
     * @param phone
     * @return
     */
    boolean send(Map<String, Object> param, String phone);
}
