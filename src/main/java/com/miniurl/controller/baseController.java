package com.miniurl.controller;

import com.miniurl.entity.Urlmap;
import com.miniurl.entity.User;
import com.miniurl.utils.RequestLimit;
import com.miniurl.service.urlmapService;
import com.miniurl.utils.CommonJson;
import com.miniurl.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private HttpServletRequest request;
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
            @RequestParam(name = "resourse_id",defaultValue = "") String resourse_id,
            @RequestParam(name = "id_ttl",defaultValue = "0") Integer id_ttl,
            @RequestParam(name = "client",defaultValue = "web") String client
    ){
        String UID="0";
        if(!token.equals("0")&&!redisUtils.hasKey("token:"+token)){
            return CommonJson.success(new HashMap<>(){{
                put("msg","用户未登录或登录超时");
            }});
        }else if (!token.equals("0")) {
            User userInDB=userService.getById(redisUtils.get("token:"+token).toString());
            if(userInDB==null){
                return CommonJson.success(new HashMap<>(){{
                    put("msg","系统错误");//有token但是没有注册
                }});
            }else if(!userInDB.getUserEmailVerify().equals("1")){
                return CommonJson.success(new HashMap<>(){{
                    put("urlToLoginEmail", "http://mail."+ StringUtils.substringAfter(userInDB.getUserEmail(),"@"));
                    put("msg","未验证邮箱");
                }});
            }
            redisUtils.expire("token:"+token, 7200);
            UID=userInDB.getUserId().toString();
        }
        String regex = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(original_url).matches()&& original_url.equals("")) {
            return CommonJson.failure("非法地址");
        }
        String finalUID = UID;
        Urlmap temp=new Urlmap(){{
            setOriginalUrl(original_url);
            setIdTtl(id_ttl);
            setRevision("0");
            setCreatedByUid(finalUID);
            setCreatedTime(new Date());
            setCreatedByClient(client);
        }};
        String resourse_idOut=urlmapService.add(temp);
        if(!resourse_idOut.equals("-1")){
            return CommonJson.success(new HashMap<String,String>(){{
                put("result_url","http://" + request.getServerName()+ ":"+
                        request.getServerPort()+ request.getRequestURI().replace("/createURL","")+
                        "/"+resourse_idOut
                );
                put("resourse_id",resourse_idOut);
                put("ttl",id_ttl.toString());
            }});
        }
        return CommonJson.failure("申请失败");
    }
    @GetMapping("/{id}")
    public void redirect(HttpServletResponse response, @PathVariable String id) throws IOException {
        Urlmap temp=urlmapService.getByID(new Urlmap(){{setResourseId(id);}});
        if(temp!=null) {
            response.sendRedirect(temp.getOriginalUrl());
        }else {
            response.sendRedirect("/404.html");
        }
    }
}
