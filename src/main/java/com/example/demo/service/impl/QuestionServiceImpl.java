package com.example.demo.service.impl;

import com.example.demo.entity.Question;
import com.example.demo.entity.User;
import com.example.demo.enums.PostStatusEnum;
import com.example.demo.enums.ReputationEnum;
import com.example.demo.repository.AnswerRepository;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Question saveQuestion(Question question) {
        //发布问题
        if (question.getId() == null){
            Question returnQuestion = questionRepository.save(question);
            returnQuestion.setGuid("/questions/" + returnQuestion.getId());

            //修改用户积分
            User originalUser = question.getUser();
            Integer reputation = originalUser.getReputation();
            originalUser.setReputation(reputation + ReputationEnum.PUBLISH_QUESTION.getCode());
            userRepository.save(originalUser);
            return returnQuestion;
        }else{
            //修改问题
            return questionRepository.save(question);
        }
    }

    @Override
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    @Override
    public Question getQuestionById(Long id) {
        return questionRepository.findById(id).get();
    }

    @Override
    public Page<Question> listQuestionsByStatusIn(List<String> statusList, Pageable pageable) {
        return questionRepository.findByStatusIn(statusList,pageable);
    }

    @Override
    public Page<Question> listQuestionsByStatusInAndKeywords(List<String> statusList, String keywords, Pageable pageable) {
        return questionRepository.findByStatusInAndTitleLikeOrStatusInAndTagsLike(statusList,"%" + keywords + "%",statusList,"%" + keywords +"%",pageable);
    }

    @Override
    public Page<Question> listUnansweredQuestionByStatusIn(List<String> statusList, Pageable pageable) {
        return questionRepository.findByAnswerSizeAndStatusIn(0,statusList,pageable);
    }

    @Override
    public Page<Question> listQuestionsByUserAndStatusIn(User user, List<String> statusList, Pageable pageable) {
        return questionRepository.findByUserAndStatusIn(user,statusList,pageable);
    }

    @Override
    public void viewIncrease(Long id) {
        //添加当前文章的cookie
        Question question = questionRepository.findById(id).get();
        if (question == null){
            return;
        }
        question.setViewSize(question.getViewSize() + 1);
        this.saveQuestion(question);
    }

    @Override
    public Page<Question> listQuestionsByTag(String name, Pageable pageable) {
        return questionRepository.findByTagsLike("%" + name + "%",pageable);
    }

    @Override
    public Page<Question> listAllQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    @Override
    public void removeQuestion(Long id) {
        Question originalQuestion = questionRepository.findById(id).get();
        if (Objects.equals(originalQuestion.getStatus(), PostStatusEnum.PUBLISH_POST.getCode())){
        originalQuestion.setStatus(PostStatusEnum.DELETED_POST.getCode());
        questionRepository.save(originalQuestion);

        //修改用户积分
        User originalUser = originalQuestion.getUser();
        Integer reputation = originalUser.getReputation();
        originalUser.setReputation(reputation - ReputationEnum.PUBLISH_QUESTION.getCode());
        userRepository.save(originalUser);

        //删除评论
        answerRepository.deleteByQuestion(originalQuestion);
        }else {
            questionRepository.deleteById(id);
        }
    }
}
