package com.miniurl.config;

import com.miniurl.utils.CommonJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerConfig {
    private static final Logger logger = LoggerFactory.getLogger("ExceptionHandlerConfig");
    @ExceptionHandler(value =java.sql.SQLSyntaxErrorException.class)
    public CommonJson exceptionHandler(RuntimeException  e){
        logger.warn("数据库错误：\n"+e);
        return CommonJson.success("致命错误");
    }
}