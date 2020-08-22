package com.example.demo.service;

import com.example.demo.VO.UserRankVO;
import com.example.demo.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RankService {
    /**
     * 获得用户排行信息
     */
    UserRankVO  getUserRankVO(User user);

    /**
     * 用户回答榜
     */
    List<UserRankVO> getUserAnswerRankTop10();

    /**
     * 用户文章榜
     */
    List<UserRankVO> getUserArticleRankTop10();

    /**
     * 用户提问榜
     * @return
     */
    List<UserRankVO> getUserQuestionRankTop10();

    /**
     * 用户声望榜
     */
    List<UserRankVO> getUserReputationTop10();
}
