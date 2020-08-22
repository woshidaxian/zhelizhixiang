package com.example.demo.VO;

import lombok.Data;

@Data
public class MessageVO {
    private Long id;

    private String content;//内容

    private Integer uid;//私信人的id

    private String name;//私信人的昵称

    private String avatar;//私信人的头像

    private String createTime;//发布时间

    private Integer count;//该好友发的私信数量

    public MessageVO() {
    }

    public MessageVO(Long id, String content, Integer uid, String name,String avatar, String createTime) {
        this.id = id;
        this.content = content;
        this.uid = uid;
        this.name = name;
        this.avatar = avatar;
        this.createTime = createTime;
    }
}
