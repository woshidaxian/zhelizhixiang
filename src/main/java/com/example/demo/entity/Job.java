package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
@Entity
@Table(name = "job")
@Data
public class Job {
    private static final long serialVersionUID = -9005722255873746978L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "职位名称不能为空")
    @Column(length = 20,unique = true)
    private String name; // 职位名称
}
