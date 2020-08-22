package com.example.demo.dto;

import com.example.demo.entity.BindType;
import lombok.Data;

@Data
public class BindUserDTO {
    private String openId;

    private String nickname;

    private String gender;

    private String avatar;

    private BindType bindType;
}
