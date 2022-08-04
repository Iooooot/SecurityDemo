package com.wht.controller;

import com.wht.entity.ResponseEntityDemo;
import com.wht.entity.User;
import com.wht.service.LoginServcie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private LoginServcie loginServcie;

    @PostMapping("/login")
    public ResponseEntityDemo login(@RequestBody User user){
        //登录
        return loginServcie.login(user);
    }

    @RequestMapping("/logout")
    public ResponseEntityDemo logout(){
        return loginServcie.logout();
    }
}
