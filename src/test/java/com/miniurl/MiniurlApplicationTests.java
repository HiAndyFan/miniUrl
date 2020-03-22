package com.miniurl;

import com.miniurl.entity.Urlmap;
import com.miniurl.entity.User;
import com.miniurl.mapper.UrlmapMapper;
import com.miniurl.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;

import static com.miniurl.pojo.MiniUrlGenerate.MiniUrlGenerate;

@SpringBootTest
class MiniurlApplicationTests {
    @Autowired
    private UrlmapMapper urlmapMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void password(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String encodePasswod = encoder.encode("zeqide@163.com");
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
    @Autowired
    private UserMapper userMapper;
    @Test
    void test1(){
        //Urlmap urlmap=new Urlmap(){{setResourseId("twxgf");}};
        //System.out.println(urlmapMapper.selectByPrimaryKey(urlmap).getOriginalUrl());
    }
}
