package com.miniurl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import tk.mybatis.spring.annotation.MapperScan;
//exclude = DataSourceAutoConfiguration.class
@SpringBootApplication()
@MapperScan(value = "com.miniurl.mapper")
@ComponentScan("com.miniurl.*")//扫utils
public class MiniurlApplication {
    public static void main(String[] args) {
        SpringApplication.run(MiniurlApplication.class, args);
    }
}
