package com.example.demo.entity;

import com.example.demo.utils.DateUtil;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 操作提醒的用户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private User fromUser;

    /**
     * 接受提醒的用户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser;

    /**
     * 提醒类型
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_type_id")
    private NoticeType noticeType;

    /**
     * 页面url
     */
    @Column(name = "guid")
    private String guid;

    @Transient
    private Integer count;

    /**
     * 内容
     */
    private String content;

    /**
     * 补充
     */
    private String more;

    @CreationTimestamp
    @Column(name = "create_time")
    private Timestamp createTime;

    private Integer status = 0;//0未阅读，1已阅读

    @Transient
    public String easyCreateTime;

    public String getEasyCreateTime(){
        if (getCreateTime() == null){
            return null;
        }
        return DateUtil.getRelativeDate(getCreateTime());
    }



}
