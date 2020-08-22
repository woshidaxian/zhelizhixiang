package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.PermissionService;
import com.example.demo.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    @Override
    public User addAdministrator(User user) {
        if (user.getId() != null){
            //更新用户
            User originalUser = userService.getUserById(user.getId());
            //getAuthorities 得到的是角色名称，不是角色对象
            BeanUtils.copyProperties(user,originalUser);
            return userRepository.save(originalUser);
        }else {
            //创建用户
            return userRepository.save(user);
        }
    }
}

