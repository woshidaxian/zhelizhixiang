package com.example.demo.controller;

import com.example.demo.VO.Response;
import com.example.demo.entity.Article;
import com.example.demo.entity.Bookmark;
import com.example.demo.entity.User;
import com.example.demo.enums.SiteTitleEnum;
import com.example.demo.service.ArticleService;
import com.example.demo.service.BookmarkService;
import com.example.demo.service.UserService;
import com.example.demo.utils.ConstraintViolationExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

@Controller
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
public class BookmarkController {
    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;


    /**
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping("/manage/bookmarks")
    public ModelAndView list(@RequestParam(value = "async", required = false, defaultValue = "false") Boolean async,
                             @RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PageRequest pageRequest = PageRequest.of(pageIndex - 1, pageSize);
        Page<Bookmark> bookmarkPage = bookmarkService.findBookmarkByUser(user, pageRequest);
        model.addAttribute("page", bookmarkPage);
        model.addAttribute("site_title", SiteTitleEnum.BOOKMARK_MANAGE.getTitle());
        return new ModelAndView("home/geren :: #to_shoucang");
    }

    /**
     * 添加书签
     *
     * @param articleId
     * @return
     */
    @PostMapping("/bookmarks")
    public ResponseEntity<Response> createBookmark(Long articleId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();        user = userService.getUserByUsername(user.getUsername());
        Article article = null;
        try {
            //1、判断文章是否存在
            article = articleService.getArticleById(articleId);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, "文章不存在！"));
        }
        //2、判断是否收藏过了
        if (bookmarkService.isMarkArticle(user, article)) {
            return ResponseEntity.ok().body(new Response(false, "您已经收藏过了，请在用户中心查看！"));
        }
        try {
            //3、添加收藏
            Bookmark bookmark = new Bookmark(user, article);
            bookmarkService.saveBookmark(bookmark);

        } catch (ConstraintViolationException e) {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }
        return ResponseEntity.ok().body(new Response(true, "收藏成功，请在用户中心查看！"));
    }
}
