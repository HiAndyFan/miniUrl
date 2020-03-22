package com.miniurl.service;

import com.miniurl.entity.Urlmap;

import java.util.List;

public interface urlmapService {
    String add(Urlmap urlmap);
    boolean delete(Urlmap urlmap);
    boolean update(Urlmap urlmap);
    Urlmap getByID(Urlmap urlmap);
    List<Urlmap> getAllByPage(Integer currentPage, Integer pageSize);
}
