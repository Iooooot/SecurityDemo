package com.wht.service;


import com.wht.entity.ResponseEntityDemo;
import com.wht.entity.User;

public interface LoginServcie {
    ResponseEntityDemo login(User user);

    ResponseEntityDemo logout();

}
