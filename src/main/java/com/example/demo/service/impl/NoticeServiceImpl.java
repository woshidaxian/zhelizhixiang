package com.example.demo.service.impl;

import com.example.demo.entity.Notice;
import com.example.demo.entity.NoticeType;
import com.example.demo.entity.User;
import com.example.demo.enums.NoticeStatusEnum;
import com.example.demo.repository.NoticeRepository;
import com.example.demo.repository.NoticeTypeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private NoticeTypeRepository noticeTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Transient
    @Override
    public void clearNoticesByUser(User touser) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user = userRepository.findByUsername(user.getUsername());
        List<Notice> noticeList = noticeRepository.findByToUser(user);
        noticeRepository.deleteAll(noticeList);
    }

    @Override
    public void createNotice(Notice notice) {
        noticeRepository.save(notice);
    }

    @Override
    public void markreadNoticesByUser(User user) {
        User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Notice> noticeList = noticeRepository.findByToUserAndStatus(principal, NoticeStatusEnum.NOT_READ.getCode());
        for (int i = 0; i < noticeList.size(); i++){
            noticeList.get(i).setStatus(NoticeStatusEnum.HAD_READ.getCode());
        }
        noticeRepository.saveAll(noticeList);
    }

    @Override
    public Page<Notice> listNoticeByUser(User user, Pageable pageable) {
        return noticeRepository.findByToUser(user,pageable);
    }

    @Override
    public List<NoticeType> mergeAndListNewNotices(User user) {
        //根据NoticeType分类合并通知
        List<Integer> countList = noticeRepository.getCountListByNotRead(user.getId());
        List<Integer> typeIds = noticeRepository.getTypeIdSByNotRead(user.getId());
        List<NoticeType> noticeTypeList = noticeTypeRepository.findByIdInOrderById(typeIds);
        for (int i = 0; i < countList.size(); i++){
            noticeTypeList.get(i).setCount(Integer.valueOf(countList.get(i)+""));
        }
        return noticeTypeList;
    }

    @Override
    @Cacheable(value = "10m",key = "'noticeSize:'+#p0.id")
    public Integer countNotReadNotices(User user) {
        return noticeRepository.countByToUserAndStatus(user,NoticeStatusEnum.HAD_READ.getCode());
    }
}
