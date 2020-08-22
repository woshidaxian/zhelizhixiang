package com.example.demo.service.impl;

import com.example.demo.entity.LoginRecord;
import com.example.demo.entity.User;
import com.example.demo.repository.LoginRecordRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.LoginRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class LoginRecordServiceImpl implements LoginRecordService {
    @Autowired
    private LoginRecordRepository loginRecordRepository;

    @Autowired
    private UserRepository userRepository;
    @Override
    public List<LoginRecord> listTop50LoginRecords(User user) {
        return loginRecordRepository.findTop50ByUserOrderByIdDesc(user);
    }

    @Override
    public Page<LoginRecord> listLoginRecords(User user, Pageable pageable) {
        return loginRecordRepository.findByUser(user,pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<LoginRecord> listAllLoginRecords(Pageable pageable) {
        return loginRecordRepository.findAll(pageable);
    }

    @Override
    public void saveLoginRecord(LoginRecord loginRecord) {
        //添加登陆记录
        LoginRecord returnLoginRecord = loginRecordRepository.save(loginRecord);
        //修改用户最后登陆时间
        User user = loginRecord.getUser();
        user.setLastLoginTime(returnLoginRecord.getCreateTime());
        userRepository.save(user);
    }

    @Override
    public void deleteLoginRecord(Long id) {
        loginRecordRepository.deleteById(id);
    }
}
