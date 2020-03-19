package com.miniurl.service;

import com.miniurl.entity.User;

public interface userService {
    Boolean add(User user);
    Boolean verify(User user);
}
