package com.example.demo.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptUtil {

    /**
     * 对密码进行加密
     * @param password
     * @return
     */
    public static String encode(String password){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String hashPass = bCryptPasswordEncoder.encode(password);
        return hashPass;
    }

    /**
     * 对原密码和已加密的密码进行匹配，判断是否相等
     * @param password
     * @param encodedPassword
     * @return
     */
    public static boolean match(String password,String encodedPassword){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean result = bCryptPasswordEncoder.matches(password,encodedPassword);
        return result;
    }

}
