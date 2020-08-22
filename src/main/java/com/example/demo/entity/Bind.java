package com.example.demo.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
@Entity
@Table(name = "bind")
@Data
public class Bind {
    private static final long serialVersionUID = 1746031292139389937L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bind_type_id")
    private BindType bindType;

    @Column(unique = true)
    @NotEmpty
    private String identifier;//标识符

    private String credential;//密码

    @CreationTimestamp
    @Column(name = "create_time")
    private Timestamp createTime;

    private Integer status = 1;
}
