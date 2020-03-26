package com.miniurl.utils;

public class RequestLimitException extends Exception {
    private static final long serialVersionUID = 1364225358754654702L;
    public RequestLimitException(){
        super("HTTP请求超出限制");
    }
    public RequestLimitException(String message){
        super(message);
    }
}