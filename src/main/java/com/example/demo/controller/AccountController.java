package com.example.demo.controller;

import com.example.demo.VO.Response;
import com.example.demo.VO.ResultVO;
import com.example.demo.VO.UserSessionVO;
import com.example.demo.entity.*;
import com.example.demo.enums.NoticeTypeEnum;
import com.example.demo.enums.PostStatusEnum;
import com.example.demo.enums.SiteTitleEnum;
import com.example.demo.repository.NoticeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.*;
import com.example.demo.service.impl.MailService;
import com.example.demo.service.impl.UserServiceImpl;
import com.example.demo.utils.ConstraintViolationExceptionHandler;
import com.example.demo.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/manage")
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JobService jobService;

    @Autowired
    private MailRetrieveService mailRetrieveService;

    @Autowired
    private MailService mailService;

    @Autowired
    private LoginRecordService loginRecordService;

    @Autowired
    private BindService bindService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RelationshipService relationshipService;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private RedisOperator redis;

    public final static String SITE_NAME = "ZhiXiang";

//    @GetMapping
//    public String index() {
//        return "forward:/manage/account/profile";
//    }


    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
    public ModelAndView homepage(@RequestParam(value = "keywords", required = false, defaultValue = "") String keywords,
                                 @RequestParam(value = "orderby", required = false, defaultValue = "new") String orderby,
                                 @RequestParam(value = "async", required = false, defaultValue = "false") Boolean async,
                                 @RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                Principal principal,
                                Model model) {

        //1、用户资料
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Sort sort1 = new Sort(Sort.Direction.DESC,"isSticky","id");

        PageRequest pageRequest = PageRequest.of(pageIndex - 1,pageSize,sort1);
        String postStatus = "";

        //用户全部文章
        Page<Article> userpage = null;
        List<String> statusList = new ArrayList<>();

        statusList.add(PostStatusEnum.PUBLISH_POST.getCode());
        statusList.add(PostStatusEnum.DRAFT_POST.getCode());
        statusList.add(PostStatusEnum.PRIVATE_POST.getCode());
        postStatus = "all";


        String keywords1 = null;
        userpage = articleService.listArticleByUserAndStatusIn(statusList,pageRequest, user);
        model.addAttribute("mypage",userpage);
        //model.addAttribute("site_title",SiteTitleEnum.BLOG_MANAGE.getTitle());
        model.addAttribute("mystatus",postStatus);
        model.addAttribute("mykeywords",keywords);
        System.out.println("文章列表:"+userpage+user);
        return new ModelAndView("geren");

    }


    @GetMapping("/post")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
    public ModelAndView post(@RequestParam(value = "keywords", required = false, defaultValue = "") String keywords,
                                 @RequestParam(value = "orderby", required = false, defaultValue = "new") String orderby,
                                 @RequestParam(value = "async", required = false, defaultValue = "false") Boolean async,
                                 @RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                 Model model) {

        //1、用户资料
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Sort sort1 = new Sort(Sort.Direction.DESC,"isSticky","id");

        PageRequest pageRequest1 = PageRequest.of(pageIndex - 1,pageSize,sort1);
        String postStatus = "";

        //用户全部文章
        Page<Article> userpage = null;
        List<String> statusList1 = new ArrayList<>();

        statusList1.add(PostStatusEnum.PUBLISH_POST.getCode());
        statusList1.add(PostStatusEnum.DRAFT_POST.getCode());
        statusList1.add(PostStatusEnum.PRIVATE_POST.getCode());
        postStatus = "all";


        String keywords1 = null;
        userpage = articleService.listArticleByUserAndStatusIn(statusList1,pageRequest1, user);
        model.addAttribute("mypage",userpage);
        //model.addAttribute("site_title",SiteTitleEnum.BLOG_MANAGE.getTitle());
        model.addAttribute("mystatus",postStatus);
        model.addAttribute("mykeywords",keywords);
        System.out.println("文章列表:"+userpage+user);
        return new ModelAndView("home/geren :: #to_fatie");

    }

    /**
     * 个人中心
     *
     * @param model
     * @return
     */
    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
    public ModelAndView profile(@RequestParam(value = "async", required = false) boolean async,
                                Principal principal,
                                Model model) {
        User originalUser = userService.getUserByUsername(principal.getName());

        model.addAttribute("user", originalUser);
        List<Job> jobList = jobService.listJobs();
        model.addAttribute("jobList", jobList);
        model.addAttribute("site_title", SiteTitleEnum.USER_SPACE_PROFILE.getTitle());
        return new ModelAndView("home/geren :: #to_bianji");
    }

    @GetMapping("/edit")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Response> saveBasicProfile(User user, Principal principal, HttpSession session,HttpServletRequest request){
        String nickname = request.getParameter("nickname");
        //String profile = request.getParameter("profile");
        //int sex = (Integer) request.getParameter("sex");
        //用户资料
        User originalUser = userService.getUserByUsername(principal.getName());
        System.out.println(user);
        originalUser.setNickname(user.getNickname());
        originalUser.setHomepage(user.getHomepage());
        //originalUser.setGithub(user.getGithub());
        originalUser.setContact(user.getContact());
        originalUser.setSex(user.getSex());
        //originalUser.setJob(user.getJob());
        String profile = user.getProfile();
        //处理profile
        if (profile != null){
            originalUser.setProfile(profile);
        }

        //2、修改资料，捕获异常
        try {
            userService.saveUser(originalUser);
            //3、修改Session
            UserSessionVO userSessionVO = (UserSessionVO) session.getAttribute("user");
            userSessionVO.setNickname(user.getNickname());
            userSessionVO.setUsername(originalUser.getUsername());
            session.setAttribute("user", userSessionVO);
        } catch (TransactionSystemException e) {
            e.printStackTrace();
            Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t instanceof ConstraintViolationException) {
                return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage((ConstraintViolationException) t)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true, "修改成功"));
    }

    @GetMapping("/set")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
    public ModelAndView set(Model model) {
        return new ModelAndView("home/geren :: #to_yinsi");
    }

    @GetMapping("/homepage/{name}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
    public ModelAndView homepage(@PathVariable(value = "name") String username,Model model){
        List<String> statusList = new ArrayList<>();
        System.out.println("主页"+username);
        statusList.add(PostStatusEnum.PUBLISH_POST.getCode());
        statusList.add(PostStatusEnum.DRAFT_POST.getCode());
        statusList.add(PostStatusEnum.PRIVATE_POST.getCode());
        User user = userService.getUserByUsername(username);
        List<Article> articleList = articleService.listArticleByUserAndStatusIn(statusList,user);
        model.addAttribute("article",articleList);
        model.addAttribute("user",user);
        System.out.println("主页"+username+articleList+user);
        return new ModelAndView("home/geren :: #taren_dialog");
    }
    /**
     * 获取编辑头像的界面
     *
     * @param model
     * @return
     */
    @GetMapping("/avatar")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
    public ModelAndView avatar(@RequestParam(value = "async", required = false) boolean async, Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("site_title", SiteTitleEnum.USER_SPACE_AVATAR.getTitle());
        return new ModelAndView(async == true ? "":"");
    }


    /**
     * 保存头像
     *
     * @return
     */
    @PostMapping("/avatar")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")  // 指定角色权限才能操作方法
    public ResponseEntity<Response> saveAvatar(@Param("avatarFile") MultipartFile avatarFile,
                                               Principal principal,
                                               HttpSession session) throws IOException {
        //1、后缀过滤，只支持 jpg,jpeg,png,bmp,gif
        String filename = avatarFile.getOriginalFilename();
        String suffix = (filename.substring(filename.lastIndexOf("."))).toLowerCase();
        if (!".jpg".equals(suffix) && !".png".equals(suffix) && !".jpeg".equals(suffix) && !".gif".equals(suffix) && !".bmp".equals(suffix)) {
            return ResponseEntity.ok().body(new Response(false, "文件格式不允许"));
        }

        //2、文件大小过滤
        if (avatarFile.getSize() > 1 * 1024 * 1024) {
            return ResponseEntity.ok().body(new Response(false, "文件太大，请选择小于2MB的图"));
        }

        return ResponseEntity.ok().body(new Response(true, "上传成功", avatarFile));
    }

    @GetMapping("/follow")
    @ResponseBody
    public String follow(@RequestParam(value = "name") String username) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(username);
        int uid = user.getId();
        Relationship relationship = new Relationship(principal.getId(), uid);
        relationshipService.saveRelationship(relationship);
        return null;
    }

}
