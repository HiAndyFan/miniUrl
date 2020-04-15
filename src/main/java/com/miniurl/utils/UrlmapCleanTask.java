package com.miniurl.utils;

import com.miniurl.entity.Urlmap;
import com.miniurl.mapper.UrlmapMapper;
import com.miniurl.service.urlmapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class UrlmapCleanTask {
    @Autowired
    UrlmapMapper urlmapMapper;
    @Autowired
    urlmapService urlmapService;
    private static final Logger logger = LoggerFactory.getLogger("UrlmapCleanTask");
    @Scheduled(fixedRate=1000*60*60*2)
    //@Scheduled(fixedRate=1000*60*5)
    private void configureTasks() {
        logger.info("开始urlmap清理: " + LocalDateTime.now());
        Integer num=0,fail=0;
        List<Urlmap> buffer=urlmapMapper.selectAll();
        for(Urlmap i : buffer) {
            if(i.getIdTtl()==0) continue;
            if((new Date().getTime()-i.getCreatedTime().getTime())/(1000*3600*24)>=i.getIdTtl()){
                if(!urlmapService.delete(i))
                {
                    fail++;
                }
                num++;
            }
        }
        logger.info("结束urlmap清理: 清理了" +num+"条过期记录 "+fail+"条失败  "+ LocalDateTime.now());
        //List<Urlmap> buffer=urlmapMapper.selectByExample(example);
    }

}
