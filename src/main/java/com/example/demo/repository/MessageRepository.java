package com.example.demo.repository;

import com.example.demo.entity.Message;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    /**
     * 获取聊天列表
     * @param integer
     * @return
     */
    @Query(value = "SELECT * FROM  message WHERE id IN (SELECT max (id) AS id FROM message WHERE user_id = ?1 AND status != 3 GROUP By friend_id) ORDER BY id DESC",nativeQuery= true)
    List<Message> getChatlList(Integer integer);

    /**
     * 聊天详情页
     * @param user
     * @param friend
     * @param statusList
     * @return
     */
    List<Message> findByUserAndFriendAndStatusIn(User user,User friend,List<Integer> statusList);

    /**
     * 查找未读消息的最后一条
     * @param user
     * @param friend
     * @param statusList
     * @return
     */
    Message findTopByUserAndFriendAndStatusInOrderByIdDesc(User user,User friend,List<Integer> statusList);

    /**
     * 获取未读私信的总数
     * @param user
     * @param status
     * @return
     */
    Integer countByUserAndStatus(User user,Integer status);


    /**
     * 查询未读的列表(每个好友只显示一个)
     * @param userId
     * @param status
     * @return
     */
    @Query(value = "SELECT * FROM message WHERE id IN(SELECT MAX (id) As id FROM message WHERE user_id =?1 ANd status =?2 GROUP BY friend_id) ORDER BY id DESC ",nativeQuery = true)
    List<Message> findByUserAndStatus(Integer userId,Integer status);

    /**
     *
     * @param userId
     * @param status
     * @return
     */
    @Query(value = "SELECT count(id) FROM message where user_id = ?1 AND status = ?2 GROUP BY friend_id ORDER BY MAX (id) DESC ",nativeQuery = true)
    List<Integer> listCountNotReadMessageSize(Integer userId,Integer status);

    /**
     * 获得未阅读的私信列表
     * @param user
     * @param status
     * @return
     */
    List<Message> findByUserAndStatus(User user,Integer status);
}
