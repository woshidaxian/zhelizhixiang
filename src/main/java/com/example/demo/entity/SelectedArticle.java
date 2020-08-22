package com.example.demo.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

@Entity
@Table(name = "selectedArticle")
@Data
public class SelectedArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String title;

    @Column
    private String guid;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @CreationTimestamp
    @Column
    private Timestamp createTime;

    @Column
    @Pattern(regexp = "已上线|已下线")
    private String status = "已上线";

}
