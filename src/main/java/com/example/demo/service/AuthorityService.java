package com.example.demo.service;

import com.example.demo.entity.Authority;
import com.example.demo.entity.User;

import java.util.List;

public interface AuthorityService {
    /**
     * 根据id获取Authoriy
     * @param id
     * @return
     */
    Authority getAuthorityById(Integer id);

    List<User> listUserByAuthoriy(int id);
}
