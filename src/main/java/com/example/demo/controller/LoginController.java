package com.example.demo.controller;

import com.example.demo.VO.Response;
import com.example.demo.VO.ResultVO;
import com.example.demo.controller.common.BaseController;
import com.example.demo.dto.BindUserDTO;
import com.example.demo.entity.*;
import com.example.demo.enums.SiteTitleEnum;
import com.example.demo.repository.BindRepository;
import com.example.demo.repository.JobRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthorityService;
import com.example.demo.service.MailRetrieveService;
import com.example.demo.service.impl.MailService;
import com.example.demo.service.UserService;
import com.example.demo.utils.BCryptUtil;
import com.google.code.kaptcha.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class LoginController extends BaseController {
    @Autowired
    private UserService userService;


    //角色(用户)的值
    private static final Integer ROLE_USER_AUTHORITY_ID = 2;

    //未设置职业的值
    private static final Integer JOB_NOTSET_ID = 1;


    //保留字符
    private static final String[] DISABLED_USERNAME = {"admins", "user", "users", "category", "categorys", "search", "bulletin", "notice", "forum", "question", "questions", "answer", "answers", "manage", "manages", "post", "posts", "article", "articles", "blog", "blogs", "ajax", "static/css", "js", "forum", "static", "public", "resource", "login", "logout", "forget", "resetpass", "reset", "template", "components", "static/css", "static/img", "images", "js", "getKaptchaImage", "匿名", "管理员", "站长", "版主"};

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private MailRetrieveService mailRetrieveService;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    private MailService mailService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private BindRepository bindRepository;

    /**
     *登陆页面显示
     * @return loginpape 登陆页面
     */
    @PostMapping("/login")
    public String login(Model model,HttpServletRequest request,User user){
        model.addAttribute("site_tite", SiteTitleEnum.LOGIN_PAGE.getTitle());
        return "index";
    }

    /**
     * 注册成功
     */
    @GetMapping("/register-success")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")//指定角色权限才能操作方法
    public String registerSuccess(Principal principal,Model model){
        User user = userService.getUserByUsername(principal.getName());
        Long count = userService.countUser();
        model.addAttribute("site_title",SiteTitleEnum.REGISTER_SUCCESS_PAGE.getTitle());
        model.addAttribute("user",user);
        model.addAttribute("count",count);
        System.out.println("注册成功!");
        return "index";
    }

    @PostMapping("user/register")
    public ResponseEntity<Response> registerUser(User user,HttpServletRequest request){
        String confirm_password = request.getParameter("confirm_password");

        BindUserDTO bindUser = (BindUserDTO) request.getSession().getAttribute("bindUser");

        //验证用户名长度
        if (user.getUsername() != null){
            if (user.getUsername().trim().length() < 4 || user.getUsername().trim().length() > 20){
                return ResponseEntity.ok().body(new Response(false,"用户名长度不合法"));
            }
        }else{
            return ResponseEntity.ok().body(new Response(false,"用户名不可为空"));
        }

        //验证密码长度
        if (user.getPassword() != null){
            if (user.getPassword().trim().length() < 4 || user.getPassword().trim().length() > 20){
                return ResponseEntity.ok().body(new Response(false,"密码至少4位，且包含字母"));
            }else if (!user.getPassword().equals(confirm_password)){
                return ResponseEntity.ok().body(new Response(false,"密码不一样"));
            }
        }else{
            return ResponseEntity.ok().body(new Response(false,"密码不可为空"));
        }

        //验证用户名可用性
        for (String str : DISABLED_USERNAME){
            if (str.equals(user.getUsername())){
                return ResponseEntity.ok().body(new Response(false,"用户名不合法，包含保留字符"));
            }
        }

        //验证用户名是否存在
        if (userRepository.findByUsername(user.getUsername()) != null){
            return ResponseEntity.ok().body(new Response(false,"用户名已被注册！"));
        }



        //添加权限
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID));

        user.setAuthorities(authorities);
        if (bindUser != null){
            user.setNickname(bindUser.getNickname());
            user.setAvatar(bindUser.getAvatar());
        }else{
            user.setNickname(user.getUsername());
            //user.setAvatar(AvatarUtil.getGravatar(user.getEmail()));
        }
        String password = user.getPassword();
        user.setEncodePassword(password);

//        //添加职业
//        user.setJob(jobRepository.findById(JOB_NOTSET_ID).get());
//        User backUser = null;
//        try {
//            //添加用户
//            backUser = userService.saveUser(user);
//            //创建对应的积分等级数据
//        }catch (ConstraintViolationException e){
//            e.printStackTrace();
//            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
//        }catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
//        }
//        user.setNickname("ffaaasaf");
        //user.setEmail("1468fsaffff87@qq.com");
        userService.saveUser(user);

        //进行授权登陆
        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(),password);
            token.setDetails(new WebAuthenticationDetails(request));
            Authentication authenticatedUser = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,SecurityContextHolder.getContext());
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("注册");
//        //添加第三方绑定
//        if (bindUser != null){
//            BindType bindType = bindUser.getBindType();
//            Bind bind = new Bind();
//            bind.setBindType(bindType);
//            bind.setUser(backUser);
//            bind.setIdentifier(bindUser.getOpenId());
//            bindRepository.save(bind);
//        }

        //数据
        Long count = userService.countUser();
        return ResponseEntity.ok().body(new Response(true,"注册成功",count));
    }

    /**
     * 找回密码页面显示
     *
     * @return
     */
    @GetMapping("/forget")
    public String forget(Model model) {
        model.addAttribute("site_title", SiteTitleEnum.FORGET_PAGE.getTitle());
        return "";
    }

    /**
     * 退出登陆
     * @param request
     * @param response
     * @param model
     * @return
     */
    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response,Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request,response,auth);
            //清楚记住我的cookie
            Cookie cookie = new Cookie("remember-me",null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        model.addAttribute("sise_title",SiteTitleEnum.LOGIN_PAGE.getTitle());
        return "redirect:";
    }


    /**
     * 忘记密码提交,然后跳转登录页面
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/forget")
    public ResponseEntity<Response> forgetUser(HttpServletRequest request, String username, String email) {
        String expect = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (expect != null && !expect.equalsIgnoreCase(request.getParameter("kaptcha"))) {
            return ResponseEntity.ok().body(new Response(false, "验证码错误"));
        }
        User user = userRepository.findByUsernameAndEmail(username, email);
        //1、判断用户是否存在
        if (user == null) {
            return ResponseEntity.ok().body(new Response(false, "用户名或邮箱不正确!"));
        }
        //2、判断是否验证了邮箱
        if (!Objects.equals(user.getIsVerifyEmail(), "Y")) {
            return ResponseEntity.ok().body(new Response(false, "该账号邮箱没有验证，无法找回，请联系管理员!"));
        }

        //2、如果已发送，且时间在60s内，则提示
        MailRetrieve mailRetrieve = mailRetrieveService.getMailRetrieveByAccount(username);
        if (mailRetrieve != null && (System.currentTimeMillis() - mailRetrieve.getCreateTime()) < 60 * 1000) {
            return ResponseEntity.ok().body(new Response(false, "邮件已发送，请稍后再试!"));
        }
        //3、生成链接
        String basePath = "";
        if (request.getServerPort() == 80) {
            basePath = "http://.com/resetpass";
        } else {
            basePath = "http://.com:" + request.getServerPort() + "/resetpass";
        }
        String mailUrl = mailRetrieveService.getEmailUrl(basePath, username);
        //4、发送邮件
        String receiver = user.getEmail();//接收者
        String subject = "【" + SITE_NAME + "】重置密码";//邮件主题(标题)
        StringBuilder content = new StringBuilder();
        content.append("尊敬的用户" + user.getNickname() + "，您好：<br/>");
        content.append("您正在" + SITE_NAME + "进行重置密码操作，请您点击下面的链接完成重置<br/>");
        content.append("<a href=" + mailUrl + " target=\"_blank\">" + mailUrl + "</a><br/><br/>");
        content.append("若这不是您本人要求的，请忽略本邮件，一切如常。<br/><br/>");
        mailService.sendHtmlMail(receiver, subject, content.toString());
        return ResponseEntity.ok().body(new Response(true, "发送成功"));
    }


    @GetMapping("/verify-resetpass-error")
    public String verifyResetpassError(String username, String errorMsg, Model model) {
        model.addAttribute("username", username);
        model.addAttribute("errorMsg", errorMsg);
        model.addAttribute("site_title", SiteTitleEnum.RESET_PASSWORD_VERIFY_FAIL.getTitle());
        return "home/login/verify_resetpass_error";
    }

    /**
     * 重置密码验证
     * 验证通过，显示修改密码页面
     *
     * @param sid
     * @param username
     * @return
     */
    @GetMapping(value = "/resetpass")
    public String verifyMail(String sid, String username, HttpServletRequest request, Model model) {

        ResultVO resultVO = mailRetrieveService.verifyMailUrl(sid, username);
        //验证通过,显示设置密码页面
        if (resultVO.getCode() == 0) {
            //验证成功
            model.addAttribute("username", username);
            model.addAttribute("sid", sid);
            model.addAttribute("site_title", SiteTitleEnum.RESET_PASSWORD_PAGE.getTitle());
            return "home/login/resetpass";
        } else {
            //验证失败
            String errorMsg = resultVO.getMsg();
            return "forward:/verify-resetpass-error?username=" + username + "&errorMsg=" + errorMsg;
        }
    }


    /**
     * 重置密码提交
     *
     * @param sid
     * @param username
     * @param password
     * @return
     */
    @PostMapping(value = "/resetpass")
    public ResponseEntity<Response> resetPass(String sid, String username, String password) {
        ResultVO resultVO = mailRetrieveService.verifyMailUrl(sid, username);
        //如果验证通过(防止用户自定义表单，再次验证)
        if (resultVO.getCode() == 0) {
            //修改密码
            User user = userService.getUserByUsername(username);
            user.setPassword(BCryptUtil.encode(password));
            userService.saveUser(user);
            return ResponseEntity.ok().body(new Response(true, "修改成功"));
        }
        //验证失败
        else {
            return ResponseEntity.ok().body(new Response(false, resultVO.getMsg()));
        }
    }

}



