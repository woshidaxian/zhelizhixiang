package com.example.demo.service;

import com.example.demo.entity.Relationship;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RelationshipService {
    /**
     * 列出所有的关注者
     * @param userId
     * @param pageable
     * @return
     */
    Page<User> listFollow(Integer userId, Pageable pageable);

    /**
     * 列出所有的粉丝
     * @param userId
     * @param pageable
     * @return
     */
    Page<User> listFans(Integer userId,Pageable pageable);

    /**
     * 列出互相关注的id
     * @param userId
     * @return
     */
    List<Integer> listFriends(Integer userId);

    /**
     * 添加关系
     * @param relationship
     */
    void saveRelationship(Relationship relationship);

    /**
     * 去除关系
     * @param relationship
     */
    void removeRelationship(Relationship relationship);

    /**
     * 更新关注数
     * @param userId
     */
    void updateFollowSize(Integer userId);

    /**
     * 更新粉丝数
     * @param userId
     */
    void updateFanSize(Integer userId);

    /**
     * 获得两个用户之间的关系,0未关注，1已关注，2互相关注
     * @param fromUserId
     * @param toUserID
     * @return
     */
    Integer getRelationshipBetweenUsers(Integer fromUserId,Integer toUserID);
}
