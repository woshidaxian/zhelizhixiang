package com.example.demo.service.impl;

import com.example.demo.VO.UserSessionVO;
import com.example.demo.entity.Bind;
import com.example.demo.entity.BindType;
import com.example.demo.entity.User;
import com.example.demo.repository.BindRepository;
import com.example.demo.service.BindService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.BindingType;
import java.util.List;

@Service
public class BindServiceImpl implements BindService {

    @Autowired
    private BindRepository bindRepository;

    @Autowired
    private UserService userService;

    @Override
    public Bind getBindById(Long id) {
        return bindRepository.findById(id).get();
    }

    @Override
    public void saveBind(Bind bind) {
        Bind temp = bindRepository.findByBindTypeAndIdentifier(bind.getBindType(),bind.getIdentifier());
        if (temp == null){
            bindRepository.save(bind);
        }
    }

    @Override
    public void removeBind(Bind bind) {
        bindRepository.delete(bind);
    }

    @Override
    public UserSessionVO getUserSessionVO(BindType bindType, String identifier) {
        User user = bindRepository.findByBindTypeAndIdentifier(bindType,identifier).getUser();
        return userService.getUserSessionVO(user);
    }

    @Override
    public List<Bind> listBinds(User user) {
        return bindRepository.findByUser(user);
    }
}
