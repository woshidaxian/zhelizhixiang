package com.example.demo.VO;

import com.example.demo.entity.User;
import lombok.Data;

import java.util.List;
@Data
public class UserRankVO
{

    private Integer id;

    private String nickname;

    private String username;

    private String avatar;

    private Long viewSize;

    private Long articleSize;

    private Long answerSize;

    private Long questionSize;

    private Integer reputation;
}
