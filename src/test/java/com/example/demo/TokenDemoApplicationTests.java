package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenDemoApplicationTests {

    @Autowired
    private UserService userService;

    //注册
    @Test
    public void register() {
        String name = "123";
        String password = "123";
        if (!userService.checkUsername(name)) {
            User user = new User();
            user.setPassword(password);
            user.setUsername(name);
            userService.register(user);
        }else {
            System.out.println("已经注册");
        }
    }

    //登陆
    @Test
    public void login(){
        String name = "123";
        String password = "123";
        userService.login(name,password);
    }

    //评论


}
