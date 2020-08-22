package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.enums.NoticeTypeEnum;
import com.example.demo.enums.PostStatusEnum;
import com.example.demo.enums.ReputationEnum;
import com.example.demo.repository.AnswerRepository;
import com.example.demo.repository.NoticeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AnswerService;
import com.example.demo.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisOperator redis;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional
    @Override
    public Answer getAnswerById(Long id) {
        return answerRepository.findById(id).get();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveAnswer(Answer answer) {
        //1.添加答案
        Answer result = answerRepository.save(answer);
        //2.添加提醒
        //如果是回答的是自己的问题,不提醒
        if (!Objects.equals(answer.getUser().getId(),answer.getReplyUser().getId())){
            Notice notice = new Notice();
            notice.setContent(answer.getQuestion().getTitle());
            notice.setMore(answer.getContent());
            if (answer.getPid() == null){
                notice.setGuid(answer.getQuestion().getGuid()+"#answer-"+result.getId());
            }else{
                notice.setGuid(answer.getQuestion().getGuid()+"#answer-"+result.getId());
            }
            notice.setFromUser(answer.getUser());
            notice.setToUser(answer.getReplyUser());
            NoticeType noticeType = new NoticeType();
            if (answer.getReplyUser().getUsername().equals(answer.getQuestion().getUser().getUsername())){
                noticeType.setId(NoticeTypeEnum.ANSWER_QUESTION.getCode());
            }else{
                noticeType.setId(NoticeTypeEnum.COMMENT_ANSWER.getCode());
            }
            notice.setNoticeType(noticeType);
            noticeRepository.save(notice);
        }
        //添加redis
        try{
            redis.incr("noticeSize"+answer.getReplyUser().getId(),1);
        }catch(Exception e){
            logger.error("redis服务故障",e);
        }

        //2修改用户积分
        User originalUser = answer.getUser();
        Integer reputation = originalUser.getReputation();
        originalUser.setReputation((reputation+ReputationEnum.PUBLISH_ANSWER.getCode()));
    }

    @Override
    public Answer updateAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    @Override
    public void removeAnswer(Long id) {
        Answer originalAnswer = answerRepository.findById(id).get();
        if (Objects.equals(originalAnswer.getStatus(), PostStatusEnum.PUBLISH_POST.getCode())){
            originalAnswer.setStatus(PostStatusEnum.DELETED_POST.getCode());
            answerRepository.save(originalAnswer);

            //修改用户积分
            User originUser = originalAnswer.getUser();
            Integer reputation = originUser.getReputation();
            originUser.setReputation(reputation - ReputationEnum.PUBLISH_ANSWER.getCode());
            userRepository.save(originUser);
        }else{
            answerRepository.deleteById(id);
        }
    }

    @Override
    public Integer countAnswerSizeByquestion(Question question) {
        List<String> statusList = new ArrayList<>();
        statusList.add(PostStatusEnum.PUBLISH_POST.getCode());
        return answerRepository.countByQuestionAndStatusIn(question,statusList);
    }

    @Transactional
    @Override
    public Answer createZan(Long answerId) {
        Answer originalAnswer = answerRepository.findById(answerId).get();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        Zan zan = new Zan(user);
        //点过赞就取消
        boolean isExist = originalAnswer.addZan(zan);

        //2、修改用户积分
        User originalUser = originalAnswer.getUser();
        Integer reputation = originalUser.getReputation();
        if (isExist){//取消赞
            originalUser.setReputation(reputation - ReputationEnum.ANSWER_GET_ZAN.getCode());
        }else{//赞
            originalUser.setReputation(reputation + ReputationEnum.ANSWER_GET_ZAN.getCode());
        }
        userRepository.save(originalUser);
        return answerRepository.save(originalAnswer);
    }

    @Transactional
    @Override
    public Answer createCai(Long answerId) {
        Answer originalAnswer = answerRepository.findById(answerId).get();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cai cai = new Cai(user);
        //点过踩就取消
        boolean isExit = originalAnswer.addCai(cai);

        //修改用户积分
        User originalUser = originalAnswer.getUser();
        Integer reputation = originalUser.getReputation();
        if (isExit){//取消踩
            originalUser.setReputation(reputation - ReputationEnum.ANSWER_GET_CAI.getCode());
        }else{//踩
            originalUser.setReputation(reputation + ReputationEnum.ANSWER_GET_CAI.getCode());
        }
        userRepository.save(originalUser);
        return answerRepository.save(originalAnswer);
    }

    @Override
    public Page<Answer> listAnswerByStatusIn(List<String> statusList, Pageable pageable) {
        return answerRepository.findByStatusIn(statusList,pageable);
    }

    @Override
    public Page<Answer> listAnswerBystatusInAndKeyWord(List<String> statusList, String keywords, Pageable pageable) {
        return answerRepository.findByContentLikeAndStatusIn("%"+keywords+"%",statusList,pageable);
    }

    @Override
    public Page<Answer> listAnswerByQuestionAndStatusIn(Question question, List<String> statusList, Pageable pageable) {
        return answerRepository.findByQuestionAndPidAndStatusIn(question,null,statusList,pageable);
    }

    @Override
    public Long countAnswerBystatus(String status) {
        return answerRepository.countByStatus(status);
    }

    @Override
    public Page<Answer> listSendAnswers(User user, List<String> statusList, Pageable pageable) {
        return answerRepository.findByUserAndStatusIn(user,statusList,pageable);
    }

    @Override
    public Page<Answer> listRecieveAnswers(User user, List<String> statusList, Pageable pageable) {
        return answerRepository.findByReplyUserAndStatusIn(user,statusList,pageable);
    }

    @Override
    public Long countByUserAndStatusIn(User user, List<String> statusList) {
        return answerRepository.countByUserAndStatusIn(user,statusList);
    }

    @Override
    public Long countByReplyUserAndStatusIn(User user, List<String> statusList) {
        return answerRepository.countByReplyUserAndStatusIn(user,statusList);
    }

    @Override
    public Page<Answer> listAllAnswerByUserAndStatusIn(User user, List<String> statusList, Pageable pageable) {
        return answerRepository.findByUserAndStatusIn(user,statusList,pageable);
    }

    @Override
    public Page<Answer> listAnswerByUserAndStatusIn(User user, List<String> statusList, Pageable pageable) {
        return answerRepository.findByUserAndPidIsNullAndStatusIn(user,statusList,pageable);
    }

    @Override
    public Page<Answer> listAnswersByUserAndIsAcceptedAndStatusIn(User user, Integer isAccepted, List<String> statusList, Pageable pageable) {
        return answerRepository.findByUserAndPidIsNullAndIsAcceptedAndStatusIn(user,isAccepted,statusList,pageable);
    }

    @Override
    public Page<Answer> listAllAnswer(Pageable pageable) {
        return answerRepository.findAll(pageable);
    }
}
