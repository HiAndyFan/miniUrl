package com.miniurl.controller;

import com.miniurl.entity.Urlmap;
import com.miniurl.service.urlmapService;
import com.miniurl.utils.CommonJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class baseController {
    @Autowired
    private urlmapService urlmapService;

    @PostMapping("/createURL")
    public CommonJson createURL(
            @RequestParam(name = "session",defaultValue = "0") String session,
            @RequestParam(name = "original_url") String original_url,
            @RequestParam(name = "resourse_id",defaultValue = "") String resourse_id,
            @RequestParam(name = "id_ttl",defaultValue = "0") Integer id_ttl
    ){
        String UID="0";
        Urlmap temp=new Urlmap(){{
            setOriginalUrl(original_url);
            setIdTtl(id_ttl);
            setRevision("0");
            setCreatedByUid(UID);
            setCreatedTime(new Date());
            setUpdatedTime(new Date());
        }};
        String resourse_idOut=urlmapService.add(temp);
        if(resourse_idOut!="-1"){
            HashMap<String,String> buffer=new HashMap<String,String>(){{
                put("resourse_id",resourse_idOut);
                put("ttl",id_ttl.toString());
            }};
            return CommonJson.success(buffer);
        }
        return CommonJson.failure("申请失败");
    }
    @GetMapping("/404")
    public void notFound(){
        System.out.println("404");
    }
    @GetMapping("/{id}")
    public void redirect(HttpServletResponse response, @PathVariable String id) throws IOException {
        //System.out.println("跳转："+id);
        Urlmap temp=urlmapService.getByID(id);
        if(temp==null){
            response.sendRedirect("/404");
        }else{
            response.sendRedirect(temp.getOriginalUrl());
        }
    }
}
