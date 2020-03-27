package com.miniurl.service.impl;

import com.miniurl.entity.Urlmap;
import com.miniurl.entity.User;
import com.miniurl.mapper.UrlmapMapper;
import com.miniurl.service.urlmapService;
import com.miniurl.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import static com.miniurl.pojo.MiniUrlGenerate.MiniUrlGenerate;
@Service
public class urlmapServiceImpl implements urlmapService {
    private final static int SUCCESS = 1;
    @Autowired
    private UrlmapMapper urlmapMapper;

    @Override
    public String add(Urlmap urlmap) {
        String id=MiniUrlGenerate();
        Urlmap temp=urlmapMapper.selectByPrimaryKey(id);
        while (temp!=null) {
            id = MiniUrlGenerate();
            temp = urlmapMapper.selectByPrimaryKey(id);
        }
        urlmap.setResourseId(id);
        if(urlmapMapper.insert(urlmap) == SUCCESS) return id;
        else return "-1";
    }

    @Override
    public boolean delete(Urlmap urlmap) {
        return urlmapMapper.deleteByPrimaryKey(urlmap) == SUCCESS;
    }

    @Override
    public boolean update(Urlmap urlmap) {
        return urlmapMapper.updateByPrimaryKey(urlmap) == SUCCESS;
    }

    @Override
    public Urlmap getByID(Urlmap urlmap) {
        return urlmapMapper.selectByPrimaryKey(urlmap);
    }

    @Override
    public List<Urlmap> getAllByPage(Integer currentPage, Integer pageSize) {
        List<Urlmap> urlmaps = urlmapMapper.selectAll();
        Integer countNums = urlmapMapper.selectCount(new Urlmap());
        PageBean<Urlmap> page = new PageBean<>(currentPage,pageSize,countNums);
        page.setItems(urlmaps);
        return page.getItems();
    }

    @Override
    public List<Urlmap> getByPage(User user, Integer currentPage, Integer pageSize){
        List<Urlmap> urlmaps = urlmapMapper.select(new Urlmap(){{
            setCreatedByUid(user.getUserId().toString());
        }});
        Integer countNums = urlmapMapper.selectCount(new Urlmap());
        PageBean<Urlmap> page = new PageBean<>(currentPage,pageSize,countNums);
        page.setItems(urlmaps);
        return page.getItems();
    }
}
