package com.example.demo.repository;

import com.example.demo.entity.Article;
import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
   /**
    * 统计某篇文章有多少条评论
    * @param article
    * @param statusList
    * @return
    */
   Integer countByArticleAndStatusIn(Article article, List<String> statusList);

   /**
    * 我的回复的评论列表
    * @param user
    * @return
    */
   List<Comment> findByUser(User user);

   /**
    * 回复的评论列表
    * @param user
    * @return
    */
   List<Comment> findByReplyUser(User user);

   /**
    * 获得某篇文章某些状态的一级评论
    * @param article
    * @param pid
    * @param statusList
    * @param pageable
    * @return
    */
   Page<Comment> findByArticleAndPidAndStatusIn(Article article, Long pid, List<String> statusList, Pageable pageable);

   /**
    * 获得某篇文章最大楼层号码
    * @param articleId
    * @return
    */
   @Query(value = "select max(floor) from comment where article_id =?1",nativeQuery = true)
   Integer getMaxCommentFloor(Long articleId);

   /**
    * 根据文章获得评论，用于删除
    * @param article
    * @return
    */
   List<Comment> findByArticle(Article article);
   /**
    * 根据状态获得评论
    * @param statusList
    * @param pageable
    * @return
    */
   Page<Comment> findByStatusIn(List<String> statusList,Pageable pageable);

   /**
    * 根据状态和关键词查找
    * @param content
    * @param stausList
    * @param pageable
    * @return
    */
   Page<Comment> findByContentLikeAndStatusIn(String content,List<String> stausList,Pageable pageable);

   /**
    * 用户、状态、查询评论
    * 获得某个用户发布的评论
    * @param user
    * @param statusList
    * @param pageable
    * @return
    */
   Page<Comment> findByUserAndStatusIn(User user,List<String> statusList,Pageable pageable);

   /**
    * 获得某个用户收到的评论
    * @param user
    * @param statusList
    * @param pageable
    * @return
    */
   Page<Comment> findByReplyUserAndStatusIn(User user,List<String> statusList,Pageable pageable);

   /**
    * 统计
    * @param user
    * @param statusList
    * @return
    */
   Long countByUserAndStatusIn(User user,List<String> statusList);

   /**
    * 统计收到的评论数
    * @param user
    * @param statusList
    * @return
    */
   Long countByReplyUserAndStatusIn(User user,List<String> statusList);

   /**
    * 统计某个状态评论数
    * @param status
    * @return
    */
   Long countByStatus(String status);

   /**
    * 统计文章的一级评论数
    * @param article
    * @return
    */
  // Long countByArticleAndPisIsNull(Article article);

   Integer deleteByUser(User user);

   Integer deleteByReplyUser(User user);

   Integer deleteByArticle(Article article);
}
