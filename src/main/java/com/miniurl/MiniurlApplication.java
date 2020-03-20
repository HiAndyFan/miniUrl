package com.miniurl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication()
@MapperScan(value = "com.miniurl.mapper")
public class MiniurlApplication {
    public static void main(String[] args) {
        SpringApplication.run(MiniurlApplication.class, args);
    }
}
