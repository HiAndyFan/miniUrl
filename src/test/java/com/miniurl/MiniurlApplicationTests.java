package com.miniurl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.miniurl.pojo.MiniUrlGenerate.MiniUrlGenerate;

@SpringBootTest
class MiniurlApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void password(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String encodePasswod = encoder.encode("123456");
        System.out.println(encodePasswod);

        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bcryptPasswordEncoder.matches("123456",encodePasswod));
    }
    @Test
    void generate(){
        for(int i=0;i<10;i++){
            System.out.println(MiniUrlGenerate());
        }
    }
}
