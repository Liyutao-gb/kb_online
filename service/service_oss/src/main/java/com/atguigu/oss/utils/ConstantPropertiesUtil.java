package com.atguigu.oss.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author lytstart
 * @create 2020-08-04-9:35
 */
@Component
public class ConstantPropertiesUtil implements InitializingBean {

    @Value(value = "${aliyun.oss.file.endpoint}")
    private String endpoint;

    @Value(value = "${aliyun.oss.file.keyid}")
    private String keyId;

    @Value(value = "${aliyun.oss.file.keysecret}")
    private String keySecret;

    @Value(value = "${aliyun.oss.file.bucketname}")
    private String bucketName;

    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() {
        END_POINT = endpoint;
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keySecret;
        BUCKET_NAME = bucketName;
    }
}
