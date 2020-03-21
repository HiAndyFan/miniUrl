package com.miniurl.controller;

import com.miniurl.entity.User;
import com.miniurl.service.urlmapService;
import com.miniurl.service.userService;
import com.miniurl.utils.CommonJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class userController {

    @Autowired
    userService userService;

    @PostMapping("/register")
    public CommonJson register(@RequestParam(name = "email") String email,
                               @RequestParam(name = "username") String username,
                               @RequestParam(name = "password") String password
    ){
        User buffer=new User(){{
            setUserEmail(email);
            setUserName(username);
            setHashPass(password);//明文
            setUserEmailVerify("0");
        }};
        String s=userService.add(buffer);
        if(s!="0"){
            HashMap<String,String> msgbuffer=new HashMap<String,String>(){{
                put("USER_EMAIL_VERIFY",s);
                put("msg","注册成功");
            }};
            return CommonJson.success(msgbuffer);
        }else {
            return CommonJson.failure("注册失败");
        }
    }

    @PostMapping("/confirm")
            public CommonJson confirmEmail(@RequestParam(name = "userid") String userid,
            @RequestParam(name = "check") String check
    ){
        User temp=new User(){{
            setUserId(Integer.parseInt(userid));
            setUserEmailVerify(check);
        }};
        if(userService.confirm(temp)){
            return CommonJson.success("注册成功");
        }else {
            return CommonJson.failure("注册失败");
        }
    }
}
