package com.example.demo.repository;

import com.example.demo.entity.Question;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {

    /**
     * 根据状态获取问题列表
     * @param statusList
     * @param pageable
     * @return
     */
    Page<Question> findByStatusIn(List<String> statusList, Pageable pageable);

    /**
     * 获得0回答的问题
     * @param answerSize
     * @param statusList
     * @param pageable
     * @return
     */
    Page<Question> findByAnswerSizeAndStatusIn(Integer answerSize,List<String> statusList,Pageable pageable);

    /**
     * 根据状态和用户获得问题列表
     * @param user
     * @param statusList
     * @param pageable
     * @return
     */
    Page<Question> findByUserAndStatusIn(User user,List<String> statusList,Pageable pageable);

    /**
     * 根据标签名模糊查询
     * @param tagName
     * @param pageable
     * @return
     */
    Page<Question> findByTagsLike(String tagName,Pageable pageable);

    /**
     * 获得问答提问榜用户
     */
    @Query(value = "SELECT user_id FROM question WHERE stats != 'deleted' GROUP BY user_id ORDER BY COUNT(id) DESC LIMIT 10",nativeQuery = true)
    List<Integer> getUserRankByQuestionSize();

    /**
     * 获得回答提问榜提问数
     * @return
     */
    @Query(value = "SELECT COUNT (id) as count FROM question where status != 'deleted' GROUP BY user_id ORDER BY COUNT DESC LIMIT 10",nativeQuery = true)
    List<BigInteger> getCountRankByQuestionSize();

    /**
     * 统计用户的提问数
     * @param id
     * @return
     */
    @Query(value = "SELECT COUNT (*) FROM question WHERE user_id = ?1 AND status != 'deleted'",nativeQuery = true)
    Long countQuestionSizeByUserId(Integer id);

    /**
     * 根据标签和标题查询
     * @param statusList
     * @param title
     * @param statusList2
     * @param tags
     * @param pageable
     * @return
     */
    Page<Question> findByStatusInAndTitleLikeOrStatusInAndTagsLike(List<String> statusList,String title,List<String> statusList2,String tags,Pageable pageable);

    Integer deleteByUser(User user);
}
