package com.example.demo.converter;

import com.example.demo.VO.MessageVO;
import com.example.demo.entity.Message;

public class Message2MessageVOConverter {

    public static MessageVO convert(Message message) {
        MessageVO messageVO = new MessageVO(message.getId(), message.getContent(), message.getFriend().getId(), message.getFriend().getNickname(), message.getFriend().getAvatar(), message.getEasyCreateTime());
        return messageVO;
    }
}
