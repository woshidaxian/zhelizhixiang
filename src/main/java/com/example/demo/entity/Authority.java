package com.example.demo.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;


@Entity(name = "authority")
@Data
public class Authority implements GrantedAuthority, Serializable {

    private static final long serialVersionUID = 2627785840265805178L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public  String getAuthority(){return name;}

    public void setName(String name){this.name = name;}
}
