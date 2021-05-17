package com.atguigu.msmservice.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author lytstart
 * @create 2020-08-04-9:35
 */
@Component
public class ConstantPropertiesUtil implements InitializingBean {

    /**
     * 读取配置文件内容
     */
    @Value(value = "${aliyun.msm.file.keyid}")
    private String keyId;
    @Value(value = "${aliyun.msm.file.keysecret}")
    private String keySecret;
    @Value(value = "${aliyun.msm.file.code}")
    private String code;
    @Value(value = "${aliyun.msm.file.signature}")
    private String signature;

    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String CODE;
    public static String SIGNATURE;

    @Override
    public void afterPropertiesSet() {
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keySecret;
        CODE = code;
        SIGNATURE = signature;
    }
}
