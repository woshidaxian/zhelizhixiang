package com.example.demo.entity;

import com.example.demo.utils.DateUtil;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity(name = "answer")
@Getter
@Setter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Basic
    @NotEmpty(message = "内容不能为空")
    @Size(max = 50000,message = "内容过长,请删减")
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @CreationTimestamp
    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "pid")
    private Long pid;//父分类

    @OneToMany(cascade = {CascadeType.DETACH},fetch = FetchType.LAZY)
    @JoinColumn(name = "pid",referencedColumnName = "id")
    @OrderBy("id")
    private List<Answer> replyList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status")
    @Pattern(regexp = "publish|deleted")
    private String status = "publish";

    @Column(name = "comment_size")
    private Integer commentSize = 0;

    @Column(name = "si_accepted")
    private Integer isAccepted = 0;//是否采纳，未采纳，已采纳1

    @JoinColumn(name = "reply_user_id")
    @OneToOne(fetch = FetchType.LAZY)
    private User replyUser;

    //赞的数量
    @Column(name = "zan_size")
    private Integer zanSize = 0;

    //踩的数量
    @Column(name = "cai_size")
    private Integer caiSize = 0;

    //这把要把数据库的外键删除
    @OneToMany(cascade = {CascadeType.DETACH},fetch = FetchType.LAZY)
    @JoinColumn(name = "pid",referencedColumnName = "id")
    @OrderBy("id")
    private List<Answer> answerList;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "answer_zan",joinColumns = @JoinColumn(name = "answer_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "zan_id",referencedColumnName = "id"))
    private List<Zan> zanList;

    public List<Zan> getZanList(){return zanList;}

    public void setZanList(List<Zan> zanList){
        this.zanList = zanList;
        this.zanSize = this.zanList.size();
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
                //如果点过赞了，就取消
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
     * 取消点赞
     * @param zanId
     */

    public void removeZan(Long zanId){
        for (int index = 0;index < this.zanList.size();index++){
            if (Objects.equals(this.zanList.get(index).getId(),zanId)){
                this.zanList.remove(index);
                break;
            }
        }
        this.zanSize = this.zanList.size();
    }

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "answer_cai",joinColumns = @JoinColumn(name = "answer_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "cai_id",referencedColumnName = "id"))
    private List<Cai> caiList;

    public List<Cai> getCaiList(){return caiList;}

    public void setCaiList(List<Cai> caiList){
        this.caiList = caiList;
        this.caiSize = this.caiList.size();
    }

    /**
     * 踩
     * @param cai
     * @return
     */
    public boolean addCai(Cai cai){
        boolean isExist = false;
        for (int index = 0; index < this.caiList.size();index++){
            if (Objects.equals(this.caiList.get(index).getUser().getId(),cai.getUser().getId())){
                isExist = true;
                //如果已存在,取消点赞
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
     * 取消踩
     * @param caiId
     */

    public void removeCai(Long caiId){
        for (int index = 0; index < this.caiList.size();index++){
            if (Objects.equals(this.caiList.get(index).getId(),caiId)){
                this.caiList.remove(index);
                break;
            }
        }
        this.caiSize = this.caiList.size();
    }


    @Transient
    private Integer integral;//分数或投票

    public Integer getIntegral(){return zanSize - caiSize;}

    @Transient
    public String easyCreateTime;

    public String getEasyCreateTime(){
        if (getCreateTime() == null){
            return null;
        }
        return DateUtil.getRelativeDate(getCreateTime());
    }
    public Answer(){}
    public Answer(User user,Question question,Long pid,String content){
        this.user = user;
        this.question = question;
        this.pid = pid;
        this.content = content;
    }
}
