package com.miniurl.config;

import com.miniurl.utils.CommonJson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerConfig {
    @ExceptionHandler(value =Exception.class)
    public CommonJson exceptionHandler(Exception e){
        System.out.println("未知异常："+e);
        return CommonJson.success("致命错误");
    }
}