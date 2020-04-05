package com.miniurl.controller;

import com.miniurl.entity.Urlmap;
import com.miniurl.entity.User;
import com.miniurl.service.urlmapService;
import com.miniurl.utils.CommonJson;
import com.miniurl.utils.RedisUtils;
import com.miniurl.utils.RequestLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

@RestController
public class baseController {
    @Autowired
    private urlmapService urlmapService;
    @Resource
    private RedisUtils redisUtils;
    @Autowired
    com.miniurl.service.userService userService;
    @RequestLimit(count=10,time=1800)//半个小时
    @PostMapping("/createURL")
    public CommonJson createURL(
            HttpServletRequest request,
            @RequestParam(name = "token",defaultValue = "0") String token,
            @RequestParam(name = "original_url") String original_url,
            @RequestParam(name = "resourse_id",defaultValue = "") String resourse_id,//TODO 手动设置资源码, resource拼错
            @RequestParam(name = "id_ttl",defaultValue = "7") Integer id_ttl,
            @RequestParam(name = "client",defaultValue = "web") String client
    ){
        int UID=0;
        if (!token.equals("0")) {//用户给了token
            if(!redisUtils.hasKey(token))
                return CommonJson.failure("base.TOKEN_ILLEGAL","用户未登录或登录超时");
            User user=userService.getById(redisUtils.get(token).toString());
            if(user==null){
                return CommonJson.failure("base.SYSTEM_FAIL","系统错误");//有token但是没有注册
            }else if(!user.getUserEmailVerify().equals("1")){
                return CommonJson.failure("base.USER_UNVERIFIED","用户邮箱未验证");
            }
            redisUtils.expire(token, 7200);
            UID=user.getUserId();
        } else {
            if(id_ttl>7) id_ttl =7;
        }
        String regex = "^\\S+\\.\\w{2,}";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(original_url).find()) {
            return CommonJson.failure("base.URL_ILLEGAL","输入地址非法");
        }
        int finalUID = UID;
        Integer final_id_ttl = id_ttl;
        Urlmap urlmap=new Urlmap(){{
            setOriginalUrl(original_url);
            setIdTtl(final_id_ttl);
            setRevision("0");
            setCreatedByUid(finalUID);
            setCreatedTime(new Date());
            setCreatedByClient(client);
        }};
        String resourse_idOut=urlmapService.add(urlmap);
        if(!resourse_idOut.equals("-1")){
            return CommonJson.success(new HashMap<String,String>(){{
                put("result_url","http://" + request.getServerName()+request.getRequestURI().replace("/createURL","")+
                        "/"+resourse_idOut
                );
                put("resourse_id",resourse_idOut);
                put("ttl",final_id_ttl.toString());
            }});
        }
        return CommonJson.failure("base.CREAT_MINIURL_FAIL","短链接创建失败");
    }
    @GetMapping("/{id}")
    public void redirect(HttpServletResponse response, @PathVariable String id) throws IOException {
        Urlmap urlmapResult=urlmapService.getByID(new Urlmap(){{setResourseId(id);}});
        if(urlmapResult!=null) {
            response.sendRedirect(urlmapResult.getOriginalUrl());
        }else {
            response.sendRedirect("/404");
        }
    }
}
