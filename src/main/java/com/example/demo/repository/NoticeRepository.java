package com.example.demo.repository;

import com.example.demo.entity.Notice;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends CrudRepository<Notice,Long> {
    /**
     * 获得一个用户的未阅读量的提醒，用于批量标记已阅读
     * @param toUser
     * @param status
     * @return
     */
    List<Notice> findByToUserAndStatus(User toUser,Integer status);

    /**
     * 获得一个用户的所有提醒，用于删除
     * @param user
     * @return
     */
    List<Notice> findByToUser(User user);

    /**
     * 获得用户的所有提醒，用于前台分页显示
     * @param user
     * @param pageable
     * @return
     */
    Page<Notice> findByToUser(User user, Pageable pageable);

    /**
     * 按提醒分类统计没有阅读的提醒的数量，返回列表
     * @param userId
     * @return
     */
    @Query(value = "select count(id) from notice where to_user_id = ?1 and status = 0 group by notice_type_id order by notice_type_id",nativeQuery = true)
    List<Integer> getCountListByNotRead(Integer userId);

    /**
     * 按提醒分类统计没有阅读的提醒的数量，返回列表
     * @param userId
     * @return
     */
    @Query(value = "select notice_type_if from notice where to_user_id = ?1 and status = 0 group by notice_type_id order by notice_type_id",nativeQuery = true)
    List<Integer> getTypeIdSByNotRead(Integer userId);

    /**
     * 统计未读数量
     * @param user
     * @param status
     * @return
     */
    Integer countByToUserAndStatus(User user,Integer status);

    Integer deleteByFromUser(User user);

}
