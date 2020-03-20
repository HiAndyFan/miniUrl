package com.miniurl.controller;

import com.miniurl.service.urlmapService;
import com.miniurl.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class userController {

    @Autowired
    userService userService;

    @PostMapping
    void hello(){
        System.out.println("hello");
    }
}
