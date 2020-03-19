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

    private final static int SUCCESS_CODE = 200 ;
    private final static int FAILURE_CODE = 1000 ;
    private final static String SUCCESS_MESSAGE = "请求成功";
    private final static String FAILURE_MESSAGE = "请求失败";

    private int code ;
    private Object data;
    private String message;
    private boolean success;

    public static CommonJson success(Object data){
        return success().setData(data);
    }

    public static CommonJson success(){
        return new CommonJson()
                .setCode(SUCCESS_CODE)
                .setSuccess(true)
                .setMessage(SUCCESS_MESSAGE);
    }

    public static CommonJson failure(String message){
        return failure().setData(message);
    }

    public static CommonJson failure(){
        return new CommonJson()
                .setCode(FAILURE_CODE)
                .setSuccess(false)
                .setMessage(FAILURE_MESSAGE);

    }

    /**
     * 根据boolean自动选择返回成功/失败实例
     */
    public static CommonJson expect(boolean success) {
        return success ? success() : failure();
    }

    /**
     * 配合@expect（）方法使用
     */
    public CommonJson setSuccessMessage(String message) {
        return isSuccess() ? setMessage(message) : this;
    }

    /**
     * 配合@expect（）方法使用
     */
    public CommonJson setFailureMessage(String message) {
        return !isSuccess() ? setMessage(message) : this;
    }

    /**
     * 配合@expect（）方法使用
     */
    public CommonJson setSuccessData(Object data) {
        return isSuccess() ? setData(data) : this;
    }

}
