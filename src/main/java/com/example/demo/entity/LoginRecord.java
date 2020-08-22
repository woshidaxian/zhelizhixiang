package com.example.demo.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
@Entity(name = "login_record")
@Data
public class LoginRecord implements Serializable {
    private static final long serialVersionUID = 3819695643032970580L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增长策略
    private Long id;

    @Column(name = "login_type")
    private String loginType;//登录方式

    @CreationTimestamp  // 由数据库自动创建时间
    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(length = 40)
    private String ip;//IP

    @Column(length = 100)
    private String area;//归属地

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
