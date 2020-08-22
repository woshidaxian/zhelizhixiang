package com.example.demo.repository;

import com.example.demo.entity.Answer;
import com.example.demo.entity.Question;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer,Long> {

    /**
     * 统计某篇文章有多少条回答
     * @param question
     * @param statusList
     * @return
     */
    Integer countByQuestionAndStatusIn(Question question, List<String> statusList);

    /**
     * 我的回复的回答列表
     * @param user
     * @return
     */
    List<Answer> findByUser(User user);

    /**
     * 回复的回答列表
     * @param user
     * @return
     */
    List<Answer> findByReplyUser(User user);

    /**
     * 获取某篇文章某些状态的一级回答
     * @param question
     * @param pid
     * @param statusList
     * @param pageable
     * @return
     */
    Page<Answer> findByQuestionAndPidAndStatusIn(Question question, Long pid, List<String> statusList, Pageable pageable);

    /**
     * 获得谋篇文章最大楼层号码
     * @param questionId
     * @return
     */
    @Query(value = "select max (floor) from answer where question_id = ?1",nativeQuery = true)
    Integer getMaxAnswerFloor(Long questionId);

    /**
     * 根据文章获得回答，用于删除
     * @param question
     * @return
     */
    List<Answer> findByQuestion(Question question);

    /**
     * 根据状态获得回答
     * @param statusList
     * @param pageable
     * @return
     */
    Page<Answer> findByStatusIn(List<String> statusList,Pageable pageable);

    /**
     * 根据状态和关键词查找
     * @param content
     * @param statusList
     * @param pageable
     * @return
     */
    Page<Answer> findByContentLikeAndStatusIn(String content,List<String> statusList,Pageable pageable);

    /**
     * 回答所有的回答包括回复和评
     * @param user
     * @param statusList
     * @param pageable
     * @return
     */
    Page<Answer> findByUserAndStatusIn(User user,List<String> statusList,Pageable pageable);

    /**
     * 获得所有的回答（回复和评论不要）
     * @param user
     * @param statusList
     * @param pageable
     * @return
     */
    Page<Answer> findByUserAndPidIsNullAndStatusIn(User user,List<String> statusList,Pageable pageable);

    Page<Answer> findByUserAndPidIsNullAndIsAcceptedAndStatusIn(User user,Integer isAccepted,List<String> statusList,Pageable pageable);

    /**
     * 获得某个用户收到的回答
     * @param user
     * @param statusList
     * @param pageable
     * @return
     */
    Page<Answer> findByReplyUserAndStatusIn(User user,List<String> statusList,Pageable pageable);

    /**
     * 统计
     * @param user
     * @param statusList
     * @return
     */
    Long countByUserAndStatusIn(User user,List<String> statusList);

    /**
     * 统计收到的回答数
     * @param user
     * @param statusList
     * @return
     */
    Long countByReplyUserAndStatusIn(User user,List<String> statusList);

    /**
     * 统计某个状态回答数
     * @param status
     * @return
     */
    Long countByStatus(String status);

    /**
     * 获得回答用户榜
     * @return
     */
    @Query(value = "SELECT user_id FROM answer WHERE status = 'publish' AND ISNULL(pid) GROUP BY user_id ORDER BY COUNT (id) DESC LIMIT 10",nativeQuery = true)
    List<Integer> getUserRankByAnswerSize();

    /**
     * 统计回答用户榜
     * @return
     */
    @Query(value = "SELCET COUNT(id) AS count FROM answer WHERE status = 'publish' AND ISNULL (pid) GROUP BY user_id ORDER BY count DESC LIMIT 10",nativeQuery = true)
    List<BigInteger> getCountRankByAnswerSize();

    @Query(value = "SELECT COUNT(*) FROM answer WHERE user_id = ?1 AND status !='deleted'",nativeQuery = true)
    Long countAnswerSizeByuserId(Integer id);

    Integer deleteByUser(User user);

    Integer deleteByQuestion(Question question);


}
