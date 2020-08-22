package com.example.demo.service;

import com.example.demo.entity.LoginRecord;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LoginRecordService {
    /**
     * 获取所有
     * @param user
     * @return
     */
    List<LoginRecord> listTop50LoginRecords(User user);

    /**
     * 获得用户登陆记录
     * @param user
     * @param pageable
     * @return
     */
    Page<LoginRecord> listLoginRecords(User user, Pageable pageable);

    /**
     * 获得所有用户的登记记录
     * @param pageable
     * @return
     */
    Page<LoginRecord> listAllLoginRecords(Pageable pageable);

    /**
     * 添加登陆记录
     * @param loginRecord
     */
    void saveLoginRecord(LoginRecord loginRecord);

    /**
     * 删除日志
     * @param id
     */
    void deleteLoginRecord(Long id);

}
