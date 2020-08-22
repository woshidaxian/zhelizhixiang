package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 书签,收藏
 */
@Entity(name = "bookmark")
@Data
public class Bookmark implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Article article;

    public Bookmark(){}

    public Bookmark(User user, Article article){
        this.user = user;
        this.article = article;
    }

}
