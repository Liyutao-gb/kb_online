package com.atguigu.servicebase.exceptionhandler;

import com.atguigu.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j  //日志打印
@RestControllerAdvice
public class GlobalExceptionHandler {

    //指定出现什么异常执行这个方法
    @ExceptionHandler(Exception.class)
    public R error(Exception e) {
        e.printStackTrace();
        return R.error().message("全局异常处理..");
    }

    //特定异常
    @ExceptionHandler(ArithmeticException.class)
    public R error(ArithmeticException e) {
        e.printStackTrace();
        return R.error().message("算术异常处理..");
    }

    //自定义异常
    @ExceptionHandler(GuliException.class)
    public R error(GuliException e) {
        log.error(e.getMessage()); //输出错误信息到本地文件夹中
        log.warn(e.getMessage()); //输出警告信息到本地文件夹中
        log.info(e.getMessage()); //输出信息到本地文件夹中
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
