package com.example.demo.controller;

import com.example.demo.VO.Response;
import com.example.demo.entity.Answer;
import com.example.demo.entity.Comment;
import com.example.demo.service.AnswerService;
import com.example.demo.service.ArticleService;
import com.example.demo.service.CommentService;
import com.example.demo.service.ZanService;
import com.example.demo.utils.ConstraintViolationExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.ConstraintViolationException;

@Controller
public class VoteController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private ZanService zanService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AnswerService answerService;

    /**
     * 给文章点赞
     *
     * @param articleId
     * @return
     */
    @PostMapping("/zan/article")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
    public ResponseEntity<Response> createZanForArticle(Long articleId) {

        try {
            articleService.createZan(articleId);
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true, "点赞成功", null));
    }

    /**
     * 给评论点赞
     *
     * @param commentId
     * @return
     */
    @PostMapping("/zan/comment")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
    public ResponseEntity<Response> createZanForComment(Long commentId) {

        //判读是否点赞，如果点了赞，就取消
        Comment comment = null;
        try {
            comment = commentService.createZan(commentId);
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true, "点赞成功", comment.getZanSize()));
    }

    /**
     * 给文章点赞
     *
     * @param answerId
     * @return
     */
    @PostMapping("/zan/answer")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
    public ResponseEntity<Response> createZanForAnswer(Long answerId) {

        Answer answer = null;
        try {
            answer = answerService.createZan(answerId);
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true, "点赞成功", answer.getIntegral()));
    }

    /**
     * 给评论点踩
     *
     * @param commentId
     * @return
     */
    @PostMapping("/cai/comment")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
    public ResponseEntity<Response> createCaiForComment(Long commentId) {

        //判读是否点赞，如果点了赞，就取消
        Comment comment = null;
        try {
            comment = commentService.createCai(commentId);
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true, "点赞成功", comment.getCaiSize()));
    }

    /**
     * 给回答点踩
     *
     * @param answerId
     * @return
     */
    @PostMapping("/cai/answer")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
    public ResponseEntity<Response> createCaiForAnswer(Long answerId) {

        Answer answer = null;
        try {
            answer = answerService.createCai(answerId);
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true, "点赞成功", answer.getIntegral()));
    }

}
