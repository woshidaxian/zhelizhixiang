package com.example.demo.service;

import com.example.demo.entity.Message;
import com.example.demo.entity.User;

import java.util.List;

public interface MessageService {
    /**
     * 根据用户ID查询聊天记录
     * @param userId
     * @return
     */
    List<Message> getChatList(Integer userId);

    /**
     * 获得用户和朋友的聊天列表，不包括状态为status的
     * @param user
     * @param friend
     * @param statusList
     * @return
     */
    List<Message> getMessageList(User user,User friend,List<Integer> statusList);

    /**
     * 添加私信
     * @param message
     */
    void saveMessage(Message message);

    /**
     * 讲私信状态改为delete
     * @param id
     */
    void deleteMessage(Long id);

    /**
     * 将多个私信状态改为delete
     * @param ids
     */
    void deleteMessageBatch(List<Long> ids);

    /**
     * 删除私信
     * @param id
     */
    void removeMessage(Long id);

    /**
     * 删除多个私信
     * @param ids
     */
    void removeMessage(List<Long> ids);

    /**
     * 获得最上面一条Message
     * @param user
     * @param friend
     * @param statusList
     * @return
     */
    Message getTopMessage(User user,User friend,List<Integer> statusList);

    /**
     * 获得所有未读的数量
     * @param user
     * @return
     */
    Integer countNotReadMessageSize(User user);

    /**
     * 获得未阅读的私信列表
     * @param userId
     * @param status
     * @return
     */
    List<Message> listNotReadMessage(Integer userId,Integer status);

    /**
     * 获得未阅读私信列表的大小
     * @param userId
     * @param status
     * @return
     */
    List<Integer> listCountNotReadMessageSize(Integer userId,Integer status);
}
