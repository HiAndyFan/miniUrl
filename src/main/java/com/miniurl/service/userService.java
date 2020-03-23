package com.miniurl.service;

import com.miniurl.entity.User;

import java.util.HashMap;
import java.util.List;

public interface userService {
    HashMap<String,String> add(User user);
    boolean confirm(User user);
    String verify(User user);
    boolean delete(User user);
    boolean update(User user);
    User getById(String user_id);
    User getByEmail(String email);
    boolean comparePassword(User user,User userInDB);
    List<User> getAllByPage(Integer currentPage, Integer pageSize);
}
