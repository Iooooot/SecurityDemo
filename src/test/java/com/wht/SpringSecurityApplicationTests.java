package com.wht;

import com.wht.entity.User;
import com.wht.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
class SpringSecurityApplicationTests {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserMapper userMapper;

    @Test
    void testEncode() {
        System.out.println(passwordEncoder.encode("123456"));
    }

    @Test
    void testMapper(){
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

}
