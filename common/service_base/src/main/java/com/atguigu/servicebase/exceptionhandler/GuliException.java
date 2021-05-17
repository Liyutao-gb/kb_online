package com.atguigu.servicebase.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lytstart
 * @create 2020-08-01-19:30
 */
@Data
@AllArgsConstructor //生成有参数的构造器
@NoArgsConstructor  //生成无参构造
public class GuliException extends RuntimeException {

    private Integer code; //状态码

    private String msg; //异常信息
}
