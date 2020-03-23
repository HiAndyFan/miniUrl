package com.miniurl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import tk.mybatis.spring.annotation.MapperScan;
//exclude = DataSourceAutoConfiguration.class
@SpringBootApplication()
@MapperScan(value = "com.miniurl.mapper")
public class MiniurlApplication {
    public static void main(String[] args) {
        SpringApplication.run(MiniurlApplication.class, args);
    }
}
