package com.example.demo.service.impl;

import com.example.demo.entity.Relationship;
import com.example.demo.entity.User;
import com.example.demo.repository.NoticeRepository;
import com.example.demo.repository.RelationshipRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RelationshipServiceImpl implements RelationshipService {
    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoticeRepository noticeRepository;


    @Override
    public Page<User> listFollow(Integer userId, Pageable pageable) {
        List<Integer> relationshipList = relationshipRepository.findByFromUserId(userId);
        Page<User> userPage = userRepository.findByIdIn(relationshipList,pageable);
        return userPage;
    }

    @Override
    public Page<User> listFans(Integer userId, Pageable pageable) {
        List<Integer> relationshipList = relationshipRepository.findByToUserId(userId);
        Page<User> userPage = userRepository.findByIdIn(relationshipList,pageable);
        return userPage;
    }

    @Override
    public List<Integer> listFriends(Integer userId) {
        List<Integer> relationshipList = relationshipRepository.findFriendsByUserId(userId);
        return relationshipList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveRelationship(Relationship relationship) {
        //1.添加关注
        relationshipRepository.save(relationship);
        //更新双方关注数和粉丝数
        updateFollowSize(relationship.getFromUserId());
        updateFanSize(relationship.getToUserId());
    }

    @Override
    public void removeRelationship(Relationship relationship) {
        //删除关系
        relationshipRepository.delete(relationship);
        //更新双方关注数和粉丝数
        updateFollowSize(relationship.getFromUserId());
        updateFanSize(relationship.getToUserId());
    }

    @Override
    public void updateFollowSize(Integer userId) {
        User user = userRepository.findById(userId).get();
        user.setFollowSize(relationshipRepository.countByFromUserId(userId));
        userRepository.save(user);
    }

    @Override
    public void updateFanSize(Integer userId) {
        User user = userRepository.findById(userId).get();
        user.setFollowSize(relationshipRepository.countByFromUserId(userId));
        userRepository.save(user);
    }

    @Override
    public Integer getRelationshipBetweenUsers(Integer fromUserId, Integer toUserID) {
        if(relationshipRepository.findByFromUserIdAndToUserId(fromUserId,toUserID) !=null){
            //已关注
            if(relationshipRepository.findByToUserIdAndFromUserId(fromUserId,toUserID) == null){
                return 1;
            }else{
                //互相关注
                return 2;
            }

        }else {
            //为关注
            return 0;
        }
    }
}
