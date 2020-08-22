package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
@Data
public class NoticeType {
    @Id
    @GeneratedValue
    private Integer id;

    private String content;

    private String style;

    @Transient
    private Integer count;
}
