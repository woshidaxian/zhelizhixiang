package com.example.demo.service;

import com.example.demo.VO.HotArticleVO;
import com.example.demo.VO.SelectedArticleVO;
import com.example.demo.entity.Article;
import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ArticleService {

    Page<Article> listArticle(Pageable pageable);

    List<Article> listArticle();

    /**
     * 用户
     *
     * @param status
     * @param pageable
     * @return
     */
    Page<Article> listArticleByUserAndStatusIn(List<String> status, Pageable pageable,User user);

    List<Article> listArticleByUserAndStatusIn(List<String> status,User user);
    /**
     * 用户
     * @param status
     * @param pageable
     * @return
     */
    Page<Article> listArticleByStatusIn(List<String> status, Pageable pageable);

    /**
     * 用户
     * @param status
     * @return
     */
    List<Article> listArticleByStatusIn(List<String> status);
    /**
     * 状态、关键词
     * @param statusList
     * @param pageable
     * @param keywords
     */

    Page<Article> listArticlesByStatusInAndKeywords(List<String> statusList,String keywords,Pageable pageable);

    /**
     * 关键词
     * @param keywords
     * @param pageable
     */
    Page<Article> listArticlesByKeywords(String keywords,Pageable pageable);

    /**
     * 用户、关键词
     * @param user
     * @param keywords
     * @param pageable
     */
    Page<Article> listArticlesByUserAndKeywords(String keywords, User user,Pageable pageable);

    /**
     * 用户、状态、关键词查询
     * @param user
     * @param statusList
     * @param keywords
     * @param pageable
     * @return
     */
    Page<Article> listArticlesByUserAndStatusInAndKeywords(User user,List<String>statusList,String keywords,Pageable pageable);
    /**
     * 是否置顶、状态
     * @param user
     * @Param sticky
     * @param status
     * @param pageable
     */
    Page<Article> listArticlesByStickyAndStatusIn(User user,Integer sticky,List<String>status,Pageable pageable);
    /**
     * @param category
     * @param status
     * @param keywords
     * @param pageable
     * @return
     */
    Page<Article> listArticleByCategoryAndStatusInAndKeywords(Category category,List<String>status,String keywords,Pageable pageable);
    /**
     * 保存Article
     * @param article
     * @return
     */
    Article saveArticle(Article article);

    /**
     * 删除Article
     * @param article
     * @return
     */
    boolean removeArticle(Article article);

    /**
     * 根据id获取Article
     * @param id
     */
    Article getArticleById(Long id);

    /**
     * 阅读量增长
     * @param id
     * @return
     */
    Article viewIncrease(Long id);

    /**
     * 点赞
     * @param articleId
     * @return
     */
    void createZan(Long articleId);

    /**
     * 获取热门文章前十
     * @return
     */
    List<HotArticleVO> listTop10HotArticles();

    List<Article> listArticles();
    /**
     *
     * @return
     */
    List<SelectedArticleVO> SeleteHotArticles();

    /**
     * 统计不同状态的文章数
     */
    Long countArticleByStatus(String status);

    /**
     * 根据标签获得文章列表
     * @param tagName
     * @param pageable
     * @return
     */
    Page<Article> listArticlesByTag(String tagName,Pageable pageable);

    void deleteByStatus(String status);

    /**
     * 批量删除
     */
    void batchRemove(List<Long>ids);
}
