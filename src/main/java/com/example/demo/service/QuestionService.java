package com.example.demo.service;

import com.example.demo.entity.Question;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface QuestionService {
    /**
     * 添加问题
     * @param question
     * @return
     */
    Question saveQuestion(Question question);

    /**
     * 删除问题
     * @param id
     */
    void deleteQuestion(Long id);

    /**
     * 获得某个问题
     * @param id
     * @return
     */
    Question getQuestionById(Long id);

    /**
     * 根据状态获得问题列表
     * @param statusList
     * @param pageable
     * @return
     */
    Page<Question> listQuestionsByStatusIn(List<String> statusList, Pageable pageable);

    Page<Question> listQuestionsByStatusInAndKeywords(List<String> statusList,String keywords,Pageable pageable);

    /**
     * 获得未回答的问题列表
     * @param statusList
     * @param pageable
     * @return
     */
    Page<Question> listUnansweredQuestionByStatusIn(List<String> statusList,Pageable pageable);

    /**
     * 根据用户获得其发布的评论
     * @param user
     * @param statusList
     * @param pageable
     * @return
     */
    Page<Question> listQuestionsByUserAndStatusIn(User user,List<String> statusList,Pageable pageable);

    /**
     * 访问量加1
     * @param id
     */
    void viewIncrease(Long id);

    /**
     * 根据标签获得问题
     * @param name
     * @param pageable
     * @return
     */
    Page<Question> listQuestionsByTag(String name,Pageable pageable);

    /**
     * 获得所有的问题
     * @param pageable
     * @return
     */
    Page<Question> listAllQuestions(Pageable pageable);

    /**
     * 删除问题
     * @param id
     */
    void removeQuestion(Long id);
}
