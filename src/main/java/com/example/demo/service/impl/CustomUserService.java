package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    //    @Override
//    public User loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
//        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
//        if (user == null) {
////            throw new BadCredentialsException("用户名或邮箱不存在");
//            // throw new UsernameNotFoundException("用户名不存在");
//        } else if ("locked".equals(user.getStatus())) { //被锁定，无法登录
//            throw new LockedException("该用户被锁定");
//        }
//        return user;
//    }

//    @Override
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(userName);
//
//        return user;
//    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //SUser对应数据库中的用户表，是最终存储用户和密码的表，可自定义
        //本例使用SUser中的email作为用户名:
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("UserName " + username + " not found");
        }
        return user;
    }

    // SecurityUser实现UserDetails并将SUser的Email映射为username
//        User securityUser = new SecurityUser(user);
//        Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
//        return securityUser;
}
