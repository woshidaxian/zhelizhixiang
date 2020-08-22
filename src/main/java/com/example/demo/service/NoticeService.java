package com.example.demo.service;

import com.example.demo.entity.Notice;
import com.example.demo.entity.NoticeType;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoticeService {
    /**
     * 清空提醒
     * @param user
     */
    void clearNoticesByUser(User user);

    /**
     * 添加提醒
     * @param notice
     */
    void createNotice(Notice notice);

    /**
     * 批量标记已阅读
     * @param user
     */
    void markreadNoticesByUser(User user);

    /**
     * 获得提醒列表
     * @param user
     * @param pageable
     * @return
     */

    Page<Notice> listNoticeByUser(User user, Pageable pageable);

    /**
     * 合并显示未读
     * @param user
     * @return
     */
    List<NoticeType> mergeAndListNewNotices(User user);

    /**
     * 统计未读数量
     * @param user
     * @return
     */
    Integer countNotReadNotices(User user);
}
