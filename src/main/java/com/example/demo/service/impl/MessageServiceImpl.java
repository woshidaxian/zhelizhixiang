package com.example.demo.service.impl;

import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import com.example.demo.enums.MessageStatusEnum;
import com.example.demo.repository.MessageRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Message> getChatList(Integer userId) {
       return messageRepository.getChatlList(userId);
    }

    @Override
    public List<Message> getMessageList(User user, User friend, List<Integer> statusList) {
        return messageRepository.findByUserAndFriendAndStatusIn(user,friend,statusList);
    }

    @Override
    public void saveMessage(Message message) {
        messageRepository.save(message);
    }

    @Override
    public void deleteMessage(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user = userRepository.findByUsername(user.getUsername());
        Message originalMessage = messageRepository.findById(id).get();
        if (Objects.equals(originalMessage.getUser().getId(),user.getId())){
            originalMessage.setStatus(MessageStatusEnum.DELETE_MESSAGE.getCode());
            messageRepository.save(originalMessage);
        }
    }

    @Override
    public void deleteMessageBatch(List<Long> ids) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user = userRepository.findByUsername(user.getUsername());
        for (int i = 0; i < ids.size(); i++){
            Long id = ids.get(i);
            Message originalMessage = messageRepository.findById(id).get();
            if (Objects.equals(originalMessage.getUser().getId(),user.getId())){
                originalMessage.setStatus(MessageStatusEnum.DELETE_MESSAGE.getCode());
                messageRepository.save(originalMessage);
            }
        }
    }

    @Override
    public void removeMessage(Long id) {
        messageRepository.deleteById(id);
    }

    @Override
    public void removeMessage(List<Long> ids) {
        for (int i = 0; i < ids.size();i++){
            messageRepository.deleteById(ids.get(i));
        }
    }

    @Override
    public Message getTopMessage(User user, User friend, List<Integer> statusList) {
        return messageRepository.findTopByUserAndFriendAndStatusInOrderByIdDesc(user,friend,statusList);
    }

    @Override
    @Cacheable(value = "10m",key = "'message:'+#p0.id")
    public Integer countNotReadMessageSize(User user) {
        return messageRepository.countByUserAndStatus(user,MessageStatusEnum.NOT_READ_MESSAGE.getCode());
    }

    @Override
    public List<Message> listNotReadMessage(Integer userId, Integer status) {
        return messageRepository.findByUserAndStatus(userId,status);
    }

    @Override
    public List<Integer> listCountNotReadMessageSize(Integer userId, Integer status) {
        return messageRepository.listCountNotReadMessageSize(userId,status);
    }
}
