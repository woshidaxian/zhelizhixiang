package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @Size(max = 20,min = 1,message = "标签长度为1-20个字符")
    private String name;

    private String guid;

    @Column(name = "article_size")
    private Integer Article_Size;

    private Integer position = 1;
}
