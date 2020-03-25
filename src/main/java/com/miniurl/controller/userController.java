package com.miniurl.controller;

import com.alibaba.fastjson.JSONObject;
import com.miniurl.entity.User;
import com.miniurl.pojo.JWTTools;
import com.miniurl.service.urlmapService;
import com.miniurl.service.userService;
import com.miniurl.utils.CommonJson;
import com.miniurl.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
public class userController {
    @Autowired
    userService userService;
    @Autowired
    urlmapService urlmapService;
    @Autowired
    private HttpServletRequest request;
    @Resource
    private RedisUtils redisUtils;

    @PostMapping("/register")
    public CommonJson register(@RequestBody JSONObject jsonParam){
        Pattern pattern = Pattern.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        if (!pattern.matcher(jsonParam.getString("email")).matches()||jsonParam.getString("password").length()<=5) {
            return CommonJson.failure("非法申请");
        }
        HashMap<String,String> s=userService.add(new User(){{
            setUserEmail(jsonParam.getString("username"));
            setUserName(jsonParam.getString("email"));
            setHashPass(jsonParam.getString("password"));//明文
            setUserEmailVerify("0");
        }});
        if(s.get("code")!="0"){
            if(s.get("code")=="1"){
                return CommonJson.success(new HashMap<String,String>(){{
                    put("msg","注册失败，邮箱已注册");
                }});
            }
            return CommonJson.success(new HashMap<String,String>(){{
                put("VERIFY_URL","http://" + request.getServerName()+ ":"+
                        request.getServerPort()+ request.getRequestURI().replace("/register","")+
                        "/confirm"+"?userid="+s.get("userid")+
                        "&check="+s.get("code"));
                put("urlToLoginEmail", "http://mail."+StringUtils.substringAfter(jsonParam.getString("email"),"@"));
                put("msg","注册成功，等待验证邮箱");
            }});
        }else {
            return CommonJson.failure("注册失败");
        }
    }

    @GetMapping("/confirm")
    public CommonJson confirmEmail(
            @RequestParam(name = "userid") String userid,
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
            userService.updateLoginTime(userInDataBase);
            redisUtils.set(token,userInDataBase.getUserId(),7200);
            return CommonJson.success(new HashMap<>(){{
                put("username",userInDataBase.getUserName());
                put("token",token);
            }});
        }
    }
    @GetMapping("/getinfo")
    public CommonJson getInfo(@RequestBody JSONObject jsonParam){
        String token=jsonParam.getString("token");
        if(!redisUtils.hasKey(token)){
            return CommonJson.success(new HashMap<>(){{
                put("msg","用户未登录或登录超时");
            }});
        }
        User userInDB=userService.getById(redisUtils.get(token).toString());
        if(userInDB==null){
            return CommonJson.success(new HashMap<>(){{
                put("msg","系统错误");//有token但是没有注册
            }});
        }else if(!userInDB.getUserEmailVerify().equals("1")){
            return CommonJson.success(new HashMap<>(){{
                put("urlToLoginEmail", "http://mail."+StringUtils.substringAfter(userInDB.getUserEmail(),"@"));
                put("msg","未验证邮箱");
            }});
        }
        redisUtils.expire(token, 7200);
        return CommonJson.success(new HashMap<>(){{
            put("userid",userInDB.getUserId());
            put("email",userInDB.getUserEmail());
            put("username",userInDB.getUserName());
            put("userclass",userInDB.getUserClass());
            put("createTime",new SimpleDateFormat("yyyy-MM-dd").format(userInDB.getCreatedTime()));
            put("urlnum",userInDB.getUrlNum());
        }});
    }

    @GetMapping("/geturlrecord")
    public CommonJson getUrlRecord(
            @RequestParam(name = "token")String token,
            @RequestParam(name = "currentPage",defaultValue = "1")Integer currentPage,
            @RequestParam(name ="pageSize",defaultValue = "10")Integer pageSize){
        if(!redisUtils.hasKey(token)){
            return CommonJson.success(new HashMap<>(){{
                put("msg","用户未登录或登录超时");
            }});
        }
        User userInDB=userService.getById(redisUtils.get(token).toString());
        return CommonJson.success(urlmapService.getByPage(userInDB,currentPage,pageSize));
    }
}
