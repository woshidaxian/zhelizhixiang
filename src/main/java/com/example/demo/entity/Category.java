package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class Category implements Serializable {
    private static final long serialVersionUID = -1302049482786492870L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id" ,nullable = false)
    private Long id;

    @NotEmpty(message = "分类名称不能为空")
    @Size(min = 1,max = 20,message = "分类名必须在1-20个字符")
    @Column(nullable = false)
    private String name;

    @Column(name = "is_hidden",length = 1)
    private String isHidden = "N";//前台是否隐藏

    @Column(length = 10)
    private Integer position;//排序

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "category",cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Article> articleList;//文章列表

    @Transient
    private Integer aticleSize;

    private String guid;

    public Integer getAticleSize() {
        if (articleList == null) {
            return 0;
        }
        return articleList.size();
    }

    public void setArticleSize(Integer articleSize){
        if (articleList == null){
            this.aticleSize = 0;
        }
        this.aticleSize = articleList.size();
    }

}
