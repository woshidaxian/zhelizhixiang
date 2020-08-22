package com.example.demo.controller;

import com.example.demo.VO.Response;
import com.example.demo.entity.Article;
import com.example.demo.entity.Comment;
import com.example.demo.entity.User;
import com.example.demo.enums.CommentStickStatusEnum;
import com.example.demo.enums.PostStatusEnum;
import com.example.demo.enums.SiteTitleEnum;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.service.ArticleService;
import com.example.demo.service.CommentService;
import com.example.demo.service.UserService;
import com.example.demo.utils.ConstraintViolationExceptionHandler;
import com.example.demo.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class CommentController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisOperator redis;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 我收到的评论
     *
     * @param model
     * @return
     */
    @GetMapping("/manage/comments")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
    public ModelAndView mySendCommentList(
            @RequestParam(value = "async", required = false, defaultValue = "false") Boolean async,
            @RequestParam(value = "type", required = false, defaultValue = "in") String type,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize, sort);
        List<String> statusList = new ArrayList<>();
        statusList.add(PostStatusEnum.PUBLISH_POST.getCode());
        Page<Comment> commentPage = null;
        if ("in".equals(type)) {
            commentPage = commentService.listRecieveComments(user, statusList, pageRequest);
            model.addAttribute("type", "in");
        } else {
            commentPage = commentService.listSendComments(user, statusList, pageRequest);
            model.addAttribute("type", "out");
        }
        model.addAttribute("page", commentPage);
        if (!async) {
            Long sendCommentSize = commentService.countByUserAndStatusIn(user, statusList);
            Long recieveCommentSize = commentService.countByReplyUserAndStatusIn(user, statusList);
            model.addAttribute("sendCommentSize", sendCommentSize);
            model.addAttribute("recieveCommentSize", recieveCommentSize);
        }
        model.addAttribute("site_title", SiteTitleEnum.COMMENT_MANAGE.getTitle());
        System.out.println("回复"+commentPage);
        return new ModelAndView("home/geren :: #to_huifu");
    }


    /**
     * 发表评论
     *
     * @param commentContent
     * @return
     */
    @GetMapping("/comment")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
    public ResponseEntity<Response> createComment(Principal principal,
                                                  @RequestParam(value = "content") String commentContent,
                                                  @RequestParam(value = "articleId") Long articleId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user = userService.getUserByUsername(user.getUsername());
        Long commentId = null;
        Long replyId = null;
        Article article = articleRepository.findById(articleId).get();
        System.out.println("评论成功");
        if (article == null) {
            return ResponseEntity.ok().body(new Response(false, "文章不存在!"));
        }
        try {
            //1、评论
            if (commentId == null) {
                Comment comment = new Comment();
                comment.setArticle(article);
                comment.setContent(commentContent);
                comment.setUser(user);
                comment.setReplyUser(article.getUser());
                commentService.saveComment(comment);
            } else {
                //回复
                commentService.replyComment(article, commentId, replyId, commentContent);
            }
            //2、修改文章的评论数目
            article.setCommentSize(commentService.countCommentSizeByArticle(article));
            articleRepository.save(article);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "评论成功!", user.getNickname()));
    }

}
