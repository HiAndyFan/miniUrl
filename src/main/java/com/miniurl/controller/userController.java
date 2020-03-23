package com.miniurl.controller;

import com.alibaba.fastjson.JSONObject;
import com.miniurl.entity.User;
import com.miniurl.pojo.JWTTools;
import com.miniurl.service.userService;
import com.miniurl.utils.CommonJson;
import com.miniurl.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
public class userController {

    @Autowired
    userService userService;

    @Autowired
    private HttpServletRequest request;

    @Resource
    private RedisUtils redisUtils;

    @PostMapping("/register")
    public CommonJson register(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "password") String password
    ){
        Pattern pattern = Pattern.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        if (!pattern.matcher(email).matches()||password.length()<=5) {
            return CommonJson.failure("非法申请");
        }
        HashMap<String,String> s=userService.add(new User(){{
            setUserEmail(email);
            setUserName(username);
            setHashPass(password);//明文
            setUserEmailVerify("0");
        }});
        if(s.get("code")!="0"){
            if(s.get("code")=="1"){
                return CommonJson.success(new HashMap<String,String>(){{
                    put("msg","注册失败，邮箱已注册");
                }});
            }
            return CommonJson.success(new HashMap<String,String>(){{
                put("VERIFY_URL","https://" + request.getServerName()+ ":"+
                        request.getServerPort()+ request.getRequestURI().replace("/register","")+
                        "/confirm"+"?userid="+s.get("userid")+
                        "&check="+s.get("code"));
                put("urlToLoginEmail", "http://mail."+StringUtils.substringAfter(email,"@"));
                put("msg","注册成功，等待验证邮箱");
            }});
        }else {
            return CommonJson.failure("注册失败");
        }
    }

    @GetMapping("/confirm")
    public CommonJson confirmEmail(@RequestParam(name = "userid") String userid,
            @RequestParam(name = "check") String check
    ){
        if(userService.confirm(new User(){{
            setUserId(Integer.parseInt(userid));
            setUserEmailVerify(check);
        }})){
            return CommonJson.success("验证通过");
        }else {
            return CommonJson.failure("验证失败");
        }
    }
    @PostMapping("/login")
    public CommonJson login(@RequestBody JSONObject jsonParam){
        JWTTools jwtTools=new JWTTools();
        User user=new User(){{
            setUserEmail(jsonParam.getString("email"));
            setHashPass(jsonParam.getString("password"));
        }};
        User userInDataBase = userService.getByEmail(jsonParam.getString("email"));
        if (userInDataBase == null) {
            return CommonJson.success(new HashMap<String,String>(){{
                put("msg","该邮箱未注册");
            }});
        } else if (!userService.comparePassword(user, userInDataBase)) {
            return CommonJson.success(new HashMap<>(){{
                put("msg","密码错误");
            }});
        } else {
            String token = jwtTools.getToken(userInDataBase);
            redisUtils.set(token,userInDataBase.getUserName(),7200);
            return CommonJson.success(new HashMap<>(){{
                put("username",userInDataBase.getUserName());
                put("token",token);
            }});
        }
    }
    /*
    @GetMapping("/getinfo")
    public CommonJson getinfo(){

    }*/
}
