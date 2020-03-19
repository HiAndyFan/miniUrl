package com.miniurl.service.impl;

import com.miniurl.entity.User;
import com.miniurl.mapper.UserMapper;
import com.miniurl.service.userService;
import org.springframework.beans.factory.annotation.Autowired;

public class userServiceImpl implements userService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Boolean add(User user) {
        return null;
    }
    @Override
    public Boolean verify(User user) {
        return null;
    }
}
