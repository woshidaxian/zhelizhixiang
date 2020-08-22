package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.enums.PostStatusEnum;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.SelectedArticleRepository;
import com.example.demo.service.*;
import com.example.demo.utils.BCryptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class Admin2Controller {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RankService rankService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private SelectedArticleRepository selectedArticleRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private BookmarkService bookmarkService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @GetMapping("/show")
    public String show(){
        return "show";
    }
    /**
     * 获取后台管理主页面
     *
     * @return
     */
    @GetMapping("/admin")
    public String index() {
        return "admin";
    }

    @GetMapping("/home")
    public String home(){
        return "admin/home";
    }

    @GetMapping("/administrator")
    public String dasd(){
        return "admin/administrator";
    }

    @GetMapping("/article")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")//指定角色权限才能操作的方法
    public String article(){
        return "article";
    }

    @GetMapping("/communication")
    public String communication(){
        return "admin/communication";
    }

    @GetMapping("/initiatingAxtivities")
    public String initiatingAxtivities(){
        return "admin/initiatingAxtivities";
    }

    @GetMapping("/manuscriptStatistics")
    public String manuscriptStatistics(){
        return "admin/manuscriptStatistics";
    }

    @GetMapping("/newAdmin")
    public String newAdmin(){
        return "admin/newAdmin";
    }

    @GetMapping("/newManuscript")
    public String newManuscript(){
        return "admin/newManuscript";
    }

    @GetMapping("/newsRelease")
    public String newsRelease(){
        return "admin/newsRelease";
    }

    @GetMapping("/user")
    public String user(){
        return "admin/user";
    }

    @GetMapping("/webMessage")
    public String webMessage(){
        return "admin/webMessage";
    }

    @GetMapping("/communityRecommend")
    public String communityRecommend(){
        return "admin/communityRecommend";
    }

    @GetMapping("/admin/login-success")
    public String loginSuccess(){
        System.out.println("进入home");
        return "home";
    }
    @GetMapping("/initiatingActivities")
    public String initiatingActivities(){
        return "admin/initiatingActivities";
    }

    @GetMapping("/message/add")
    @ResponseBody
    public String message(@RequestParam(value = "name")String name,
                          @RequestParam(value = "contact") String contact,
                          @RequestParam(value = "content") String content){
        System.out.println(name+contact+content);
        Feedback feedback = new Feedback();
        feedback.setContact(contact);
        feedback.setContent(content);
        feedback.setName(name);
        try{
            feedbackRepository.save(feedback);
        }catch (Exception e){
            return "0";
        }
        return "1";
    }

    @GetMapping("/communication/article")
    @ResponseBody
    public List<Article> articleList(){
        //文章列表
        List<Article> articles = null;
        List<String> statusList = new ArrayList<>();
        statusList.add(PostStatusEnum.FATIE_POST.getCode());
        articles = articleService.listArticleByStatusIn(statusList);
        System.out.println("文章"+articles);
        return articles;
    }

    @GetMapping("/communication/delete/{id}")
    @ResponseBody
    public String deleteArticle(@PathVariable(value = "id") long id,HttpServletRequest request){
        request.getSession();
        Article article = articleService.getArticleById(id);
            if (articleService.removeArticle(article)) {
                return "1";
            }else {
                return "0";
            }
    }


    //精选文章
    @GetMapping("/communityRecommend/article")
    @ResponseBody
    public List<SelectedArticle>  selectedArticle(){
        List<SelectedArticle> selectedArticles = null;
        selectedArticles = selectedArticleRepository.findAll();
        System.out.println(selectedArticles);
        return selectedArticles;
    }

    @GetMapping("/communityRecommend/delete/{id}")
    @ResponseBody
    public String deleteRecommend(@PathVariable(value = "id") long id,HttpServletRequest request){
        request.getSession();
        try {
            selectedArticleRepository.deleteById(id);
        }catch (Exception e){
            return "0";
        }
        return "1";
    }

    @GetMapping("/selectedArticle/add")
    @ResponseBody
    public String  addselectedArticle(@RequestParam(value = "title") String title,
                                      @RequestParam(value = "link") String link,
                                      HttpServletRequest request
                                      ){
        SelectedArticle selectedArticle = new SelectedArticle();
        Cookie[] cookies =  request.getCookies();
        User user = new User();
        String username = "";
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("isLogin_zx")){
                     username = cookie.getValue();
                }
            }
        }
        user = userService.getUserByUsername(username);
        selectedArticle.setUser(user);
        selectedArticle.setTitle(title);
        selectedArticle.setUser(user);
        selectedArticle.setGuid(link);
        try {
            selectedArticleRepository.save(selectedArticle);
        }catch (Exception e) {
            return "0";
        }
        return "1";
    }

    @GetMapping("/communityRecommend/status/{id}")
    @ResponseBody
    public String  changeArticle(@RequestParam(value = "status")String status,
                                 @PathVariable(value = "id") long id){

        SelectedArticle selectedArticle = selectedArticleRepository.findById(id).get();
        try {
            selectedArticle.setStatus(status);
            selectedArticleRepository.save(selectedArticle);
        }catch (Exception e){
            return "0";
        }
        return "1";
    }


    @GetMapping("/initiatingActivities/article")
    @ResponseBody
    public List<Article> Contribution(){
        List<Article> Articles = null;
        List<String> statusList = new ArrayList<>();
        statusList.add(PostStatusEnum.VERIFY_POST.getCode());
        Articles = articleService.listArticleByStatusIn(statusList);
        return Articles;
    }

    //文章通过
    @GetMapping("/initiatingActivities/pass/{id}")
    @ResponseBody
    public String passArticle(@PathVariable(value = "id") long id){
        Article article = new Article();
        article = articleService.getArticleById(id);
        article.setStatus("publish");
        articleService.saveArticle(article);
        return "1";
    }

    //添加新管理
    @GetMapping("/administrator/add")
    @ResponseBody
    public String addAdmin(@RequestParam(value = "account",required = false,defaultValue = "")String account,
                           @RequestParam(value = "password",required = false,defaultValue = "")String password,
                           @RequestParam(value = "realname",required = false,defaultValue = "")String realname,
                           @RequestParam(value = "email",required = false,defaultValue = "")String email,
                           @RequestParam(value = "city")String city){
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(1));
        User user = new User();
        try {
            user.setEmail(email);
        }catch (Exception e){
            return "0";
        }
        try {
            user.setUsername(account);
        }catch (Exception e){
            return "2";
        }
        user.setPassword(BCryptUtil.encode(password));
        user.setNickname(realname);
        user.setAuthorities(authorities);
        user.setCity(city);
        userService.saveUser(user);
        return "1";
    }

    @GetMapping("/administrator/show")
    @ResponseBody
    public List<User> xianshi(){
        List<User> adminList = null;
        adminList = authorityService.listUserByAuthoriy(1);
        return adminList;
    }

    @GetMapping("/user/show")
    @ResponseBody
    public List<User> userShow(){
        List<User> userList = null;
        userList = authorityService.listUserByAuthoriy(2);
        System.out.println(userList);
        return userList;
    }

    //删除用户和管理员
    @GetMapping("/user/delete/{id}")
    @ResponseBody
    public String userdelete(@PathVariable(value = "id") int id,HttpServletRequest request){
        request.getSession();
        User user = userService.getUserById(id);
        System.out.println(user);
        try {
            userService.removeUser(id);
        }catch (Exception e){
            return "0";
        }
        return "1";
    }

    //修改密码
    @GetMapping("/user/modifypwd/{id}")
    @ResponseBody
    public String userdelete(@PathVariable(value = "id") int id,
                             @RequestParam(value = "password")String password,
                             HttpServletRequest request){
        request.getSession();
        User user = userService.getUserById(id);
        try {
            user.setPassword(BCryptUtil.encode(password));
            userService.saveUser(user);
        }catch (Exception e){
            return "0";
        }
        return "1";
    }

    //禁用和解禁
    @GetMapping("/user/changestatus/{id}")
    @ResponseBody
    public String changeStatus(@PathVariable(value = "id") int id,
                             @RequestParam(value = "status")String status,
                             HttpServletRequest request){
        request.getSession();
        System.out.println("修改密码"+id+status);
        User user = userService.getUserById(id);
        try {
            user.setStatus(status);
            userService.saveUser(user);
        }catch (Exception e){
            return "0";
        }
        return "1";
    }

    @GetMapping("/manuscriptStatistics/article")
    @ResponseBody
    public List<Article> manuscriptStatisticsArticle(){
        List<Article> Articles = null;
        Articles = articleService.listArticle();
        return Articles;
    }

    @GetMapping("/manuscriptStatistics/status/{id}")
    @ResponseBody
    public String  changeStatus(@RequestParam(value = "status")String status,
                                 @PathVariable(value = "id") long id){
        Article article = articleService.getArticleById(id);
        System.out.println(status+id+article);
        try {
            article.setStatus(status);
            articleService.saveArticle(article);
        }catch (Exception e){
            return "0";
        }
        return "1";
    }

    @GetMapping("/message/show")
    @ResponseBody
    public List<Feedback> messageShow(){
        List<Feedback> messageList= new ArrayList<>();
        messageList = feedbackRepository.findAll();
        System.out.println(messageList);
        return messageList;
    }


    @GetMapping("/webmessage/delete/{id}")
    @ResponseBody
    public String deleteMessage(@PathVariable(value = "id") long id,HttpServletRequest request){
        request.getSession();
        try {
            feedbackRepository.deleteById(id);
        }catch (Exception e){
            return "0";
        }
        return "1";
    }

    @GetMapping("/home/show")
    @ResponseBody
    public String homeshow(){
        return "1";
    }

    @PostMapping("/admin/loginuser")
    @ResponseBody
    public Map<String, Object> login(@RequestParam(value = "city",required = false,defaultValue = "") String city,
                                     @RequestParam(value = "account",required = false,defaultValue = "") String account,
                                     @RequestParam(value = "password",required = false,defaultValue = "") String password,
                                     HttpServletResponse response, HttpServletRequest request) {
        User user = userService.getUserByUsername(account);
        List authorities = new ArrayList();
        Map<String, Object> result = new HashMap<String, Object>();

        if (user == null) {
            result.put("code", 0);
            //return ResponseEntity.ok().body(new Response(false, "用户名不存在"));
        }
        System.out.println(user.getAuthorities());
        for (int i = 0; i < user.getAuthorities().size(); i++) {
            authorities.add(user.getAuthorities().get(i).getId());
        }
        System.out.println("权限" + authorities);
        if (authorities.indexOf(1) == -1) {
            result.put("code", -1);
            //return ResponseEntity.ok().body(new Response(false, "你没有权限，请联系管理员!"));
        }
        if (!BCryptUtil.match(password, user.getPassword())) {
            // return ResponseEntity.ok().body(new Response(false,"密码错误"));
        }
        result.put("code", 1);

        Cookie login_zx = new Cookie("isLogin_zx", account);
        Cookie city_zx = new Cookie("nowCity_zx", city);
        login_zx.setPath("/");
        login_zx.setDomain(request.getServerName());
        login_zx.setMaxAge(2400);
        city_zx.setMaxAge(2400);

        city_zx.setPath("/");
        city_zx.setDomain(request.getServerName());

        response.addCookie(login_zx);
        response.addCookie(city_zx);
        //return ResponseEntity.ok().body(new Response(true,"登陆成功"));
        return result;
    }

    @GetMapping(value = "/article/addzan")
    @ResponseBody
    public String addzan(@RequestParam(name = "articleId") Long id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Article article = articleService.getArticleById(id);
        Zan zan = new Zan();
        zan.setUser(user);
        try {
            articleService.createZan(id);
        }catch (Exception e){
            return "0";
        }

        return "1";
    }

    @GetMapping(value = "/article/star")
    @ResponseBody
    public String star(@RequestParam(name = "articleId") Long id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Article article = articleService.getArticleById(id);
        Bookmark bookmark = new Bookmark();
        try {
            bookmark.setArticle(article);
            bookmark.setUser(user);
            bookmarkService.saveBookmark(bookmark);
        }catch (Exception e){
            return "0";
        }
        return "1";
    }
}
