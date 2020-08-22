package com.example.demo.service.impl;

import com.example.demo.entity.Zan;
import com.example.demo.repository.ZanRepository;
import com.example.demo.service.ZanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ZanServiceIpml implements ZanService {
    @Autowired
    private ZanRepository zanRepository;

    @Override
    @Transactional
    public void removeZan(Long id) {
        zanRepository.deleteById(id);
    }

    @Override
    public Zan getZanById(Long id) {
        return zanRepository.findById(id).get();
    }
}
