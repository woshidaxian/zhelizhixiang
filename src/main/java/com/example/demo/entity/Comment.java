package com.example.demo.entity;

import com.example.demo.utils.DateUtil;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Data
@Entity(name = "comment")
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pid",nullable = false)
    private Long pid = 0L;//父评论,如果不设置，默认为0

    @NotEmpty(message = "评论内容不能为空")
    @Size(max = 500,message = "评论内容不能多于500个字符")
    @Column(nullable = false)
    private String content;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //楼层
    private Integer floor;
    //置顶，0表示不置顶，1置顶
    @Column(name = "is_sticky")
    private Integer isSticky = 0;

    @CreationTimestamp
    @Column(name = "creat_time")
    private Timestamp createTime;

    //属于的文章
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Article_id")
    private Article article;

    //删除数据库的外键
    @OneToMany(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "pid", referencedColumnName = "id")
    @OrderBy("id")
    private List<Comment> commentList;

    @JoinColumn(name = "reply_user_id")
    @OneToOne(fetch = FetchType.LAZY)
    private User replyUser;

    //赞的数量
    @Column(name = "zan_size")
    private Integer zanSize;

    //踩的数量
    @Column(name = "cai_size")
    private Integer caiSize;

    @Pattern(regexp = "publish|draft|private|deleted")
    @Column(name = "status")
    private String status = "publish";

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "comment_zan",joinColumns = @JoinColumn(name = "comment_id",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn
            (name = "zan_id",referencedColumnName = "id"))
    private List<Zan>zanList;

    public  List<Zan> getZanList(){return zanList;}

    public void setZanlist(List<Zan> zanlist){
        this.zanList = zanlist;
        this.zanSize = this.zanList.size();
    }

    /**
     * 点赞
     * @param zan
     */
    public boolean addZan(Zan zan){
        boolean isExist = false;
        //判断重复
        for (int index = 0; index < this.zanList.size();index++){
            if (Objects.equals(this.zanList.get(index).getId(),zan.getUser().getId())){
                isExist = true;
                this.zanList.remove(index);
                break;
            }
        }
        if (!isExist){
            this.zanList.add(zan);
        }
        this.zanSize = this.zanList.size();
        return isExist;
    }

    /**
     *取消点赞
     * @zanId
     */
    public void reMoveZan(Long zanId){
        for (int index = 0 ; index < this.zanList.size();index++){
            if (Objects.equals(this.zanList.get(index),zanId)){
                this.zanList.remove(index);
                break;
            }
        }
        this.zanSize = this.zanList.size();
    }

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "comment_zan",joinColumns = @JoinColumn(name = "comment_id",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn
            (name = "zan_id",referencedColumnName = "id"))
    private List<Cai>caiList;

    public  List<Cai> getCaiList(){return caiList;}

    public void setCailist(List<Cai> caiList){
        this.caiList = caiList;
        this.caiSize = this.caiList.size();
    }

    /**
     * 踩
     * @param cai
     */
    public boolean addCai(Cai cai){
        boolean isExist = false;
        //判断重复
        for (int index = 0; index < this.caiList.size();index++){
            if (Objects.equals(this.caiList.get(index).getId(),cai.getUser().getId())){
                isExist = true;
                this.caiList.remove(index);
                break;
            }
        }
        if (!isExist){
            this.caiList.add(cai);
        }
        this.caiSize = this.caiList.size();
        return isExist;
    }

    /**
     *取消踩
     * @caiId
     */
    public void reMoveCai(Long CaiId){
        for (int index = 0 ; index < this.caiList.size();index++){
            if (Objects.equals(this.caiList.get(index),CaiId)){
                this.caiList.remove(index);
                break;
            }
        }
        this.caiSize = this.caiList.size();
    }

    @Transient
    private String easyCreateTime;
    public String getEasyCreateTime(){
        if (getCreateTime() == null){
            return null;
        }
        return DateUtil.getRelativeDate(getCreateTime());
    }
    //评论人的头像
    private String commentAuthorAvatar;
    public Comment(){}

    public Comment(User user,Article article,String content){
        this.article = article;
        this.content = content;
        this.user = user;
    }

    public Comment(User user,String content,Article article,Long pid){
        this.article = article;
        this.content = content;
        this.user = user;
        this.pid =pid;
    }

}
