package com.example.demo.controller;

import com.example.demo.VO.Response;
import com.example.demo.entity.Notice;
import com.example.demo.entity.NoticeType;
import com.example.demo.entity.Relationship;
import com.example.demo.entity.User;
import com.example.demo.enums.NoticeTypeEnum;
import com.example.demo.enums.SiteTitleEnum;
import com.example.demo.repository.NoticeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RelationshipService;
import com.example.demo.service.UserService;
import com.example.demo.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
public class RelationshipController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RelationshipService relationshipService;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redis;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    /**
     * 关注
     *
     * @param uid
     * @return
     */
    @PostMapping("/relationships/follow")
    public ResponseEntity<Response> follow(Integer uid) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(uid).get();
        if (user == null) {
            return ResponseEntity.ok().body(new Response(false, "用户不存在！"));
        }
        if (Objects.equals(uid, principal.getId())) {
            return ResponseEntity.ok().body(new Response(false, "您时时刻刻都在关注自己哦！"));
        }
        //1、添加关系
        Relationship relationship = new Relationship(principal.getId(), uid);
        relationshipService.saveRelationship(relationship);
        //2、添加提醒
        Notice notice = new Notice();
        notice.setFromUser(principal);
        notice.setToUser(user);
        NoticeType noticeType = new NoticeType();
        noticeType.setId(NoticeTypeEnum.FOLLOW_YOU.getCode());
        notice.setNoticeType(noticeType);
        notice.setGuid("/manage/relationships/fans");
        try {
            noticeRepository.save(notice);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        try {
            redis.incr("noticeSize:" + user.getId(), 1);
        } catch (Exception e) {
            logger.error("redis服务故障",e);
        }
        return ResponseEntity.ok().body(new Response(true, "操作成功"));
    }

    /**
     * 取消关注
     *
     * @param uid
     * @return
     */
    @PostMapping("/relationships/notfollow")
    public ResponseEntity<Response> notfollow(Integer uid) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findById(uid).get();
        if (user == null) {
            return ResponseEntity.ok().body(new Response(false, "用户不存在！"));

        }
        Relationship relationship = new Relationship(principal.getId(), uid);
        relationshipService.removeRelationship(relationship);

        return ResponseEntity.ok().body(new Response(true, "操作成功"));

    }

    //粉丝-关注 start

    /**
     * 查看所有的关注者
     *
     * @param async
     * @param page
     * @param size
     * @param model
     * @return
     */
    @GetMapping("/manage/follows")
    public ModelAndView follows(
            @RequestParam(value = "async", required = false) boolean async,
            @RequestParam(value = "pageIndex", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "pageSize", defaultValue = "12", required = false) Integer size,
            Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<User> userPage = relationshipService.listFollow(user.getId(), pageRequest);

        List<Integer> friendIds = relationshipService.listFriends(user.getId());
        List<User> userList = userPage.getContent();
        for (int i = 0; i < userList.size(); i++) {
            if (friendIds.contains(userList.get(i).getId())) {
                userPage.getContent().get(i).setIsFriend(2);
            }
        }
        model.addAttribute("followpage", userPage);
        model.addAttribute("dataType", "follows");
        model.addAttribute("site_title", SiteTitleEnum.RELATIONSHIP_MANAGE.getTitle());
        System.out.println("关注"+userPage);
        return new ModelAndView("home/geren :: #to_guanzhu");

    }

    /**
     * 查看所有的粉丝
     *
     * @param principal
     * @param async
     * @param page
     * @param size
     * @param model
     * @return
     */
    @GetMapping("/manage/fans")
    public ModelAndView fans(
            Principal principal,
            @RequestParam(value = "async", required = false, defaultValue = "false") boolean async,
            @RequestParam(value = "pageIndex", defaultValue = "1", required = false) Integer page,
            @RequestParam(value = "pageSize", defaultValue = "12", required = false) Integer size,
            Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<User> userPage = relationshipService.listFans(user.getId(), pageRequest);
        List<Integer> friendIds = relationshipService.listFriends(user.getId());
        List<User> userList = userPage.getContent();
        for (int i = 0; i < userList.size(); i++) {
            if (friendIds.contains(userList.get(i).getId())) {
                userPage.getContent().get(i).setIsFriend(2);
            }
        }
        model.addAttribute("fanspage", userPage);
        model.addAttribute("dataType", "fans");
        model.addAttribute("site_title", SiteTitleEnum.RELATIONSHIP_MANAGE.getTitle());
        System.out.println("粉丝"+userPage);
        return new ModelAndView("home/geren :: #to_fensi ");

    }


    /**
     * 关注或取消关注
     *
     * @param userId
     * @param optType
     * @return
     */
    @PostMapping("/manage/relationships")
    public ResponseEntity<Response> followUser(Integer userId, String optType) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //1、判断用户是否存在
        User temp = null;
        temp = userRepository.findById(userId).get();
        if (temp == null) {
            return ResponseEntity.ok().body(new Response(false, "用户不存在"));
        }
        //2、判断是关注还是取消关注
        //关注
        if ("follow".equals(optType)) {
            relationshipService.saveRelationship(new Relationship(user.getId(), userId));
        } else if ("notfollow".equals(optType)) {
            //取消关注
            relationshipService.removeRelationship(new Relationship(user.getId(), userId));
        } else {
            //非法操作
            return ResponseEntity.ok().body(new Response(false, "非法操作"));
        }
        Long fanSize = userRepository.findById(userId).get().getFanSize();
        return ResponseEntity.ok().body(new Response(true, "操作成功", fanSize));
    }

}
