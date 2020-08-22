package com.example.demo.service;

import com.example.demo.entity.Zan;

public interface ZanService {
    /**
     * 根据id获取 Zan
     * @param id
     * @return
     */
    Zan getZanById(Long id);
    /**
     * 删除Zan
     * @param id
     * @return
     */
    void removeZan(Long id);
}
