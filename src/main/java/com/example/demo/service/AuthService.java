package com.example.demo.service;

import com.example.demo.dto.BindUserDTO;

import java.io.UnsupportedEncodingException;

public interface AuthService {
    String getAccessToken(String code);

    String getOpenId(String accessToken);

    String refreshToken(String code);

    String getAuthorizationUrl() throws UnsupportedEncodingException;

    BindUserDTO getUserInfo(String accessToken, String openId);
}
