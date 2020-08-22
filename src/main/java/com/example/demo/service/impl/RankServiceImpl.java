package com.example.demo.service.impl;

import com.example.demo.VO.UserRankVO;
import com.example.demo.entity.User;
import com.example.demo.repository.AnswerRepository;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;
@Service
public class RankServiceImpl implements RankService {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Override
    @Cacheable(value = "24h",key = "'userRankVO:'+#p0.id",unless = "#user eq null ")
    public UserRankVO getUserRankVO(User user) {
        UserRankVO userRankVO = new UserRankVO();
        Long viewSize = articleRepository.countViewSizeByUserId(user.getId());
        Long articleSize = articleRepository.countArticleSizeByUserId(user.getId());
        Long questionSize = questionRepository.countQuestionSizeByUserId(user.getId());
        Long answerSize = answerRepository.countAnswerSizeByuserId(user.getId());
        Integer reputation = user.getReputation();
        userRankVO.setId(user.getId());
        userRankVO.setUsername(user.getUsername());
        userRankVO.setNickname(user.getNickname());
        userRankVO.setAvatar(user.getAvatar());
        userRankVO.setViewSize(viewSize);
        userRankVO.setAnswerSize(answerSize);
        userRankVO.setAnswerSize(answerSize);
        userRankVO.setReputation(reputation);

        //修改数据库
        user.setViewSize(viewSize);
        user.setArticleSize(articleSize);
        user.setQuestionSize(questionSize);
        user.setAnswerSize(answerSize);
        userRepository.save(user);
        return userRankVO;
    }

    //用户回答榜
    @Override
    @Cacheable(value = "24h",key = "'rank:userAnswerRankTop10'")
    public List<UserRankVO> getUserAnswerRankTop10() {
        List<Integer> uidList = answerRepository.getUserRankByAnswerSize();
        List<BigInteger> countList = answerRepository.getCountRankByAnswerSize();
        List<UserRankVO> userRankVOList = new ArrayList<>();
        for (int i = 0; i < uidList.size();i++){
            User user = userRepository.findById(uidList.get(i)).get();
            UserRankVO userRankVO = new UserRankVO();
            userRankVO.setId(user.getId());
            userRankVO.setUsername(user.getUsername());
            userRankVO.setNickname(user.getNickname());
            userRankVO.setAvatar(user.getAvatar());
            userRankVO.setAnswerSize(Long.valueOf(countList.get(i)+""));
            userRankVOList.add(userRankVO);
        }
        return userRankVOList;
    }

    //用户文章榜(七日)
    @Override
    public List<UserRankVO> getUserArticleRankTop10() {
        List<Integer> uidList = articleRepository.getUserRankByArticleSizeInSevenDay();
        List<BigInteger> countList = articleRepository.getCountRankByArticleSizeInSevenDay() ;
        List<UserRankVO> userRankVOList = new ArrayList<>();
        for (int i = 0;i < uidList.size(); i++){
            User user = userRepository.findById(uidList.get(i)).get();
            UserRankVO userRankVO = new UserRankVO();
            userRankVO.setId(user.getId());
            userRankVO.setUsername(user.getUsername());
            userRankVO.setNickname(user.getNickname());
            userRankVO.setAvatar(user.getAvatar());
            userRankVO.setArticleSize(Long.valueOf(countList.get(i)+""));
            userRankVOList.add(userRankVO);
        }
        return userRankVOList;
    }

    //用户提问榜
    @Override
    public List<UserRankVO> getUserQuestionRankTop10() {
        List<Integer> uidList = questionRepository.getUserRankByQuestionSize();
        List<BigInteger> countList = questionRepository.getCountRankByQuestionSize();
        List<UserRankVO> userRankVOList = new ArrayList<>();
        for (int i = 0; i < uidList.size(); i++){
            User user = userRepository.findById(uidList.get(i)).get();
            UserRankVO userRankVO = new UserRankVO();
            userRankVO.setId(user.getId());
            userRankVO.setUsername(user.getUsername());
            userRankVO.setNickname(user.getNickname());
            userRankVO.setAvatar(user.getAvatar());
            userRankVO.setQuestionSize(Long.valueOf(countList.get(i) + ""));
            userRankVOList.add(userRankVO);
        }
        return userRankVOList;
    }

    //用户声望榜
    @Override
    public List<UserRankVO> getUserReputationTop10() {
        List<User> userList = userRepository.getUserRankByReputation();
        List<UserRankVO> userRankVOList = new ArrayList<>();
        for (int i = 0; i < userList.size();i++){
            User user = userList.get(i);
            UserRankVO userRankVO = new UserRankVO();
            userRankVO.setId(user.getId());
            userRankVO.setUsername(user.getUsername());
            userRankVO.setNickname(user.getNickname());
            userRankVO.setReputation(user.getReputation());
            userRankVOList.add(userRankVO);
        }
        return userRankVOList;
    }
}
