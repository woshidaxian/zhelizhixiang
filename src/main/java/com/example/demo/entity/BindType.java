package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "bind_type")
@Data
public class BindType {
    private static final long serialVersionUID = 6168385705667918117L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true,length = 20)
    private String name;

    @Column(length = 100)
    private String style;
}
