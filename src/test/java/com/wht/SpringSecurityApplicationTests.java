package com.wht;

import com.wht.entity.User;
import com.wht.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.util.List;

@SpringBootTest
class SpringSecurityApplicationTests {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserMapper userMapper;

    @Test
    void testEncode() throws UnsupportedEncodingException {
        //$2a$10$Xi1iXzc2YzRSocNX3uS91OH3VELd2rZv1UP8/awIgbSGmvfsUWYfy  123
        //$2a$10$fIVaeBd33Y2fdsVmsoxKfe7t5oARWceWZbwE5bUotgD4rnuy6URY2 123456
        System.out.println(passwordEncoder.encode("123456"));
    }

    @Test
    void testMapper(){
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

}
