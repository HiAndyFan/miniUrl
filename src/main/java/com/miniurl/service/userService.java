package com.miniurl.service;

import com.miniurl.entity.User;

import java.util.List;

public interface userService {
    String add(User user);
    boolean confirm(User user);
    String verify(User user);
    boolean delete(User user);
    boolean update(User user);
    User getById(String user_id);
    User getByEmail(String email);
    void updateLoginTime(User user);
    boolean comparePassword(User user,User userInDB);
    List<User> getAllByPage(Integer currentPage, Integer pageSize);
}
