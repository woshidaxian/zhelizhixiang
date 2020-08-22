package com.example.demo.entity;

import com.example.demo.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.sql.Array;
import java.sql.Savepoint;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "article")
public class Article implements Serializable {
    //文章
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "不能为空")
    @Size(max = 200,message = "标题不能超过200")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "摘要不能为空")
    @Size(max = 2000 ,message = "摘要不能超过2000字")
    @Column(nullable = false)
    private String summary = "";

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @NotEmpty(message = "不能为空")
    @Size(max = 500000,message = "内容过长请删减")
    @Column(nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    private Timestamp updateTime;

    //允许1，不允许0
    @Column(name = "is_allow_comment",length = 1)
    private Integer isAllowComment = 1;

    //置顶1，不置顶0
    @Column(name = "is_sticky",length = 1)
    private Integer isSticky = 0;

    @Column(length = 15)
    @Pattern(regexp = "publish|draft|private|deleted|verify|takedown|recommend|shangjia|")
    private String status = "verify";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "tags",length = 100)
    @Size(max = 100,message = "关键词太多")
    private String tags;//标签

    @Column(name = "guid",length = 100)
    private String guid;//固定链接

    @Column(name = "city")
    private String city;

    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.LAZY)
    @JoinTable(name = "article_zan",joinColumns = @JoinColumn(name = "zan_id",referencedColumnName = "id"))
    private List<Zan>zanList;


    @OneToMany(cascade = {CascadeType.REMOVE},mappedBy = "article",fetch = FetchType.LAZY)
    private List<Bookmark> bookmarkList;

    @Column(name = "comment_size")
    private Integer commentSize = 0;

    @Column(name = "view_size")
    private Integer viewSize = 0;

    @Column(name = "zan_size")
    private Integer zanSize = 0;

    @Column(name = "bookmark_size")
    private Integer bookmarkSize = 0;

    public Article(){}

    public Article(String title,String summary,String content){
        this.title = title;
        this.content = content;
        this.summary = summary;
    }
    /**
     * 点赞
     * @param zan
     * @return
     */
    public boolean addZan(Zan zan){
        boolean isExist = false;
        //判断重复
        for (int index = 0;index < this.zanList.size();index++){
            if (Objects.equals(this.zanList.get(index).getUser().getId(),zan.getUser().getId())){
                isExist = true;
                break;
            }
        }
        if (!isExist){
            this.zanList.add(zan);
            this.zanSize = this.zanList.size();
        }
        return isExist;
    }
    /**
     * 取消点赞
     * @param ZanId
     */
    public void removeZan(Long ZanId){
        for(int index = 0 ;index < this.zanList.size();index++){
            if (Objects.equals(this.zanList.get(index).getId(),ZanId)){
                this.zanList.remove(index);
            }
        }
        this.zanSize = this.zanList.size();
    }

    @Transient
    public String easyCreatTime;

    public String getEasyCreateTime(){
        if (getCreateTime() == null){
            return null;
        }
        return  DateUtil.getRelativeDate(getCreateTime());
    }

    @Transient
    public List<String>getTagList(){
        if (tags != null){
            String [] arr = tags.split("|",5);
            List<String>list = Arrays.asList(arr);
            return list;
        }else {
            return null;
        }
    }
}
