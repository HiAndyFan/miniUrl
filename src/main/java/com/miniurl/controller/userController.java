package com.miniurl.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.miniurl.entity.User;
import com.miniurl.pojo.JWTTools;
import com.miniurl.service.urlmapService;
import com.miniurl.service.userService;
import com.miniurl.utils.CommonJson;
import com.miniurl.utils.RedisUtils;
import com.miniurl.utils.kaptchaUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private DefaultKaptcha captchaProducer;
    private static final Logger logger = LoggerFactory.getLogger("userController");
    @GetMapping(value = {"/register/regvalidatecode"})
    public void loginValidateCode(HttpServletRequest request, HttpServletResponse response) throws Exception{
        kaptchaUtil.validateCode(request,response,captchaProducer,"register_validate_code");
    }

    @PostMapping("/register")
    public CommonJson register(HttpServletRequest request,@RequestBody JSONObject jsonParam){
        String validateCode=jsonParam.getString("validateCode");
        String cookieValidateCode = request.getSession().getAttribute("register_validate_code").toString();
        if(cookieValidateCode == null){
            //验证码过期
            return CommonJson.failure("user.VERIFYCODE_EXPIRED","验证码过期, 请重试");
        }else if(!cookieValidateCode.equals(validateCode)||validateCode.isEmpty()){
            //验证码不正确
            return CommonJson.failure("user.WRONG_VERIFYCODE","验证码错误");
        }else if(cookieValidateCode.equals(validateCode)){
            //验证码正确
        }
        Pattern pattern = Pattern.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        if (!pattern.matcher(jsonParam.getString("email")).matches()||jsonParam.getString("password").length()<=5) {
            return CommonJson.failure("user.WRONG_EMAIL_ADDR","邮箱格式错误");
        }
        String addMsg=userService.add(new User(){{
            setUserEmail(jsonParam.getString("email"));
            setUserName(jsonParam.getString("username"));
            setHashPass(jsonParam.getString("password"));//明文
            setUserEmailVerify("0");
        }});
        if(addMsg=="userService.USER_ALREADY_EXISTS"){
            return CommonJson.failure("user.USER_ALREADY_EXISTS", "用户邮箱已注册");
        }
        if(addMsg!="ok"){
            //这里发送邮件
            return CommonJson.success(new HashMap<String,String>(){{
//                put("VERIFY_URL","http://" + request.getServerName()+request.getRequestURI().replace("/register","")+
//                        "/confirm"+"?userid="+s.get("userid")+
//                        "&check="+s.get("code"));
                put("urlToLoginEmail", "http://mail."+StringUtils.substringAfter(jsonParam.getString("email"),"@"));
                put("msg","注册成功，等待验证邮箱");
            }});
        }
        return CommonJson.failure("user.UNKNOWN_FAILURE", "未知错误");
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
            return CommonJson.success();//TODO: 返回验证邮箱页面
        }else {
            return CommonJson.failure("","");
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
            return CommonJson.failure("user.USER_NOT_EXIST", "用户不存在");
        } else if (!userService.comparePassword(user, userInDataBase)) {
            return CommonJson.failure("user.WRONG_PASSWORD", "用户名或密码错误");
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
    @PostMapping("/getinfo")
    public CommonJson getInfo(@RequestBody JSONObject jsonParam){
        String token=jsonParam.getString("token");
        if(!redisUtils.hasKey(token)){
            return CommonJson.failure("user.TOKEN_ILLEGAL", "用户未登录或登陆超时");
        }
        User userInDB=userService.getById(redisUtils.get(token).toString());
        if(userInDB==null){
            logger.warn("/getinfo有token但是没有注册");
            return CommonJson.failure("user.SYSTEM_FAIL", "系统错误");
        }else if(!userInDB.getUserEmailVerify().equals("1")){
            return CommonJson.failure("USER_UNVERIFIED", "用户邮箱未验证");
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
            return CommonJson.failure("user.TOKEN_ILLEGAL", "用户未登录或登陆超时");
        }
        User userInDB=userService.getById(redisUtils.get(token).toString());
        return CommonJson.success(urlmapService.getByPage(userInDB,currentPage,pageSize));
    }
}
