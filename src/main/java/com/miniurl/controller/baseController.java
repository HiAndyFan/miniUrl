package com.miniurl.controller;

import com.alibaba.fastjson.JSONObject;
import com.miniurl.entity.Urlmap;
import com.miniurl.service.urlmapService;
import com.miniurl.utils.CommonJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
public class baseController {
    @Autowired
    private urlmapService urlmapService;
    @Autowired
    private HttpServletRequest request;
    @PostMapping("/createURL")
    public CommonJson createURL(
            @RequestParam(name = "session",defaultValue = "0") String session,
            @RequestParam(name = "original_url") String original_url,
            @RequestParam(name = "resourse_id",defaultValue = "") String resourse_id,
            @RequestParam(name = "id_ttl",defaultValue = "0") Integer id_ttl
    ){
        String UID="0";//待优化
        String regex = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(original_url).matches()&&original_url=="") {
            return CommonJson.failure("非法地址");
        }
        Urlmap temp=new Urlmap(){{
            setOriginalUrl(original_url);
            setIdTtl(id_ttl);
            setRevision("0");
            setCreatedByUid(UID);
            setCreatedTime(new Date());
        }};
        String resourse_idOut=urlmapService.add(temp);
        if(resourse_idOut!="-1"){
            return CommonJson.success(new HashMap<String,String>(){{
                put("result_url","https://" + request.getServerName()+ ":"+
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
