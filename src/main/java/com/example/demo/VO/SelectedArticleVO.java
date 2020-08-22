package com.example.demo.VO;

import com.example.demo.entity.User;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class SelectedArticleVO {
    private Long id;

    private String title;

    private String guid;

    private Timestamp createTime;

    private String status;

    private String name;

    public SelectedArticleVO(){}
}
