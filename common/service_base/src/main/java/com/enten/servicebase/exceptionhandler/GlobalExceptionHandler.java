package com.enten.servicebase.exceptionhandler;

import com.enten.commonutils.Result;
import com.enten.servicebase.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  指定出现什么异常执行这个方法
 */
@ControllerAdvice
@Slf4j//异常写入日志文件中
public class GlobalExceptionHandler {

    /**
     * 全局异常处理(优先度最低)
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody//为了返回数据
    public Result error(Exception e){
        log.error(ExceptionUtils.getMessage(e));
        e.printStackTrace();
        return Result.error().message("执行了全局异常");
    }

    /**
     * 特定异常处理
     */
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody//为了返回数据
    public Result error(ArithmeticException e){
        e.printStackTrace();
        return Result.error().message("执行了ArithmeticException异常");
    }

    /**
     * 自定义异常
     */
    @ExceptionHandler(EntenException.class)
    @ResponseBody//为了返回数据
    public Result error(EntenException e){
        log.error(ExceptionUtils.getMessage(e));
        e.printStackTrace();
        return Result.error().code(e.getCode()).message(e.getMsg());
    }
}
