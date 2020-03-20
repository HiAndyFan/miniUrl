package com.miniurl.controller;

import com.miniurl.entity.User;
import com.miniurl.service.urlmapService;
import com.miniurl.service.userService;
import com.miniurl.utils.CommonJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class userController {

    @Autowired
    userService userService;

    @PostMapping("/register")
    public CommonJson register(@RequestParam(name = "email") String email,
                               @RequestParam(name = "password") String password
    ){
        User buffer=new User(){{
            setUserEmail(email);
            setHashPass(password);//明文
            setUserEmailVerify("0");
        }};
        String s=userService.add(buffer);
        if(s!="0"){
            return CommonJson.success(s);
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
