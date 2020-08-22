package com.example.demo.VO;

import lombok.Data;

@Data
public class UserSessionVO {
    private Integer id;

    private String nickname;

    private String username;

    private String mail;

    private String avator;

    private Integer messageSize = 0;

    private Integer noticeSize = 0;
}
