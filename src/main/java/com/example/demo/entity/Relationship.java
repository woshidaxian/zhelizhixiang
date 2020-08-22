package com.example.demo.entity;

import lombok.Data;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name = "relationship")
@IdClass(RelationshipPK.class)
@Data
public class Relationship {

    @Id
    @Column(name = "from_user_id")
    private Integer fromUserId;

    @Id
    @Column(name = "to_user_id")
    private Integer toUserId;

    public Relationship(Integer fromUserId,Integer toUserId){
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
    }

    public Relationship(){};
}
