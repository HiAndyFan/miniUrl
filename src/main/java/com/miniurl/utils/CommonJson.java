package com.miniurl.utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@Slf4j
public class CommonJson {

    private final static String SUCCESS_CODE = "ok" ;
    private final static String SUCCESS_MESSAGE = "请求成功";

    private String code ;
    private String message;
    private Object data;

    public static CommonJson success(Object data){
        return success().setData(data);
    }

    public static CommonJson success(){
        return new CommonJson()
                .setCode(SUCCESS_CODE)
                .setMessage(SUCCESS_MESSAGE);
    }

    public static CommonJson failure(String failCode, String message){
        return new CommonJson()
                .setCode(failCode)
                .setMessage(message);
    }
}
