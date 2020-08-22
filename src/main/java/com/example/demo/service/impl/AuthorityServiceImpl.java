package com.example.demo.service.impl;

import com.example.demo.entity.Authority;
import com.example.demo.entity.User;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.service.AuthorityService;
import com.example.demo.service.UserService;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private UserService userService;
    @Override
    public Authority getAuthorityById(Integer id) {
        return authorityRepository.findById(id).get();
    }

    @Override
    public List<User> listUserByAuthoriy(int id) {
        List<Integer> list = null;
        List<User> userList = new ArrayList<>();;
        list =  authorityRepository.listadministrator(id);
        for (int i = 0 ; i < list.size(); i++){
            userList.add(userService.getUserById(list.get(i)));
        }
        return userList;
    }


}
