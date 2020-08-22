package com.example.demo.controller;

import com.example.demo.VO.HotArticleVO;
import com.example.demo.VO.Response;
import com.example.demo.VO.UserRankVO;
import com.example.demo.controller.common.BaseController;
import com.example.demo.entity.*;
import com.example.demo.enums.AllowCommentEnum;
import com.example.demo.enums.PostStatusEnum;
import com.example.demo.enums.PostStickyEnum;
import com.example.demo.enums.SiteTitleEnum;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.CommentRepository;
import com.example.demo.service.*;

import com.example.demo.utils.ConstraintViolationExceptionHandler;
import com.example.demo.utils.HTMLUtil;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.security.Principal;
import java.util.*;

import static org.springframework.data.domain.PageRequest.of;


@Controller
public class AriticleController extends BaseController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RankService rankService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RelationshipService relationshipService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentRepository commentRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String hostName = "local:8080";
    /**
     * 文章列表
     *
     * @param orderby
     * @param keywords
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     */

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")//指定角色权限才能操作方法
    @GetMapping("/talkbar")
    public ModelAndView articleList(@RequestParam(value = "keywords", required = false, defaultValue = "") String keywords,
                                    @RequestParam(value = "orderby", required = false, defaultValue = "new") String orderby,
                                    @RequestParam(value = "async", required = false, defaultValue = "false") Boolean async,
                                    @RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                    Model model, HttpServletRequest request,Principal principal){


    //文章列表
        Page<Article> articlePage = null;
        Sort sort = null;
    if(orderby.equals("hot")){
        sort = new Sort(Sort.Direction.DESC,"commentSize","zanSize","viewSize","id");
    }
    //按照访问量排序
    else if (orderby.equals("view")){
        sort = new Sort(Sort.Direction.DESC,"viewSize","id");
    }
    //按评论排序
    else if (orderby.equals("comment")){
        sort = new Sort(Sort.Direction.DESC,"comment","id");
    }
    //按点赞排序
    else if(orderby.equals("like")){
        sort = new Sort(Sort.Direction.DESC,"zanSize","id");
    }else if (orderby.equals("bookmark")){
        sort = new Sort(Sort.Direction.DESC,"bookmarkSize","id");
    }
    //最新查询
    else {
        sort =new Sort(Sort.Direction.DESC,"id");
    }
        PageRequest  pageRequest  =PageRequest.of(pageIndex - 1,pageSize,sort);
        if (keywords == null){
            List<String> statusList = new ArrayList<>();
            statusList.add(PostStatusEnum.FATIE_POST.getCode());
            articlePage = articleService.listArticleByStatusIn(statusList,pageRequest);
        }else {
            List<String> statusList = new ArrayList<>();
            statusList.add(PostStatusEnum.FATIE_POST.getCode());
            statusList.add(PostStatusEnum.PUBLISH_POST.getCode());
            articlePage = articleService.listArticlesByStatusInAndKeywords(statusList,keywords,pageRequest);
        }
        model.addAttribute("page",articlePage);
        model.addAttribute("keywords",keywords);
        model.addAttribute("site_title", SiteTitleEnum.HOME_INDEX.getTitle());
        model.addAttribute("orderby",orderby);

        if (!async){
            //用户排行榜
            List<UserRankVO> userRankVOList = null;
            try{
                    userRankVOList = rankService.getUserArticleRankTop10();
            }catch (Exception e){
                logger.error("redis服务故障",e);
            }
            model.addAttribute("userArticleRankList",userRankVOList);
            //推荐关键词
//            List<Tag> tagList = null;
//            try {
//                tagList = tagService.listTags();
//            }catch (Exception e){
//                logger.error("redis服务故障",e);
//            }
//            model.addAttribute("tagList",tagList);

            //热门文章
            List<Article> hotArticleList = null;
            try {
                hotArticleList = articleService.listArticle();
            }catch (Exception e){
                logger.error("redis服务故障",e);
            }
            model.addAttribute("hotArticleList",hotArticleList);
            System.out.println("文章列表"+hotArticleList+userRankVOList+articlePage);
        }


        System.out.println("talkbar");
        return new ModelAndView(async? "home/talkbar :: # part1_l":"home/talkbar" );
    }


    /**
     * 文章详情页
     *
     * @param id
     * @param model
     * @return
     */

    @GetMapping("talkbar/article/{id}")
    @ResponseBody
    public List<Comment> getArticleById(@PathVariable("id") Long id,
                                              @RequestParam(value = "async",required = false,defaultValue = "false") Boolean async,
                                              @RequestParam(value = "ordeby",required = false,defaultValue = "new")String orderby,
                                              @RequestParam(value = "pageIndex",required = false,defaultValue = "1")Integer pageIndex,
                                              @RequestParam(value = "pageSize",required = false,defaultValue = "10") Integer pageSize,
                                              Model model) throws NotFoundException {
        User principal = null;

        //判断文章是否存在
        Article article = articleService.getArticleById(id);
        if (article == null){
            throw new NotFoundException("文章不存在！");
        }
        //判断操作用户是否是博客的所有者
        boolean isArticleOwner = false;
        String username = article.getUser().getUsername();
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("annoymouseUser")){
                principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal != null && username.equals(principal.getUsername())){
                    isArticleOwner = true;
                }
        }

        //判断用户是否可以访问该文章
        //作者无法看到被删除的文章
        if (isArticleOwner){
            if (Objects.equals(PostStatusEnum.DELETED_POST.getCode(),article.getStatus())) {
                throw new NotFoundException("文章已被删除!");
                }
            }else{
                //非作者无法看到了已发布的文章
                if (!Objects.equals(PostStatusEnum.PUBLISH_POST.getCode(),article.getStatus())){
                    throw new NotFoundException("该文章可能被作者临时隐藏，暂时无法访问！");
                }
            }

        //分类列表
        if (!async){
            List<Category> categoryList
                    = categoryService.listCategorys(article.getUser());
            model.addAttribute("categoryList",categoryList);
        }

        //5、判断是否是否关注了该用户
        Integer isFollow = 0 ;
        if (principal != null){
            isFollow = relationshipService.getRelationshipBetweenUsers(principal.getId(),article.getUser().getId());
        }
        model.addAttribute("isFollow",isFollow);

        //7、站点关键词和描述
        model.addAttribute("site_keywords",article.getTagList());
        model.addAttribute("site_content",article.getSummary());

        //6、初始化评论
        String commentOrder = "";
        List<String> statusList = new ArrayList<>();
        statusList.add(PostStatusEnum.PUBLISH_POST.getCode());
        List<Comment> commentPage = null;

        commentPage = commentRepository.findByArticle(article);
        article.setViewSize(article.getViewSize()+1);
        articleService.saveArticle(article);
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("comment",commentPage);
        System.out.println("文章详情" + result+article+statusList);
        return commentPage;
    }

    @GetMapping("/article/add")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")//指定角色权限才能操作的方法
    public ResponseEntity<Response> saveArticle(@RequestParam(value = "title") String title,
                                                @RequestParam(value = "type") long type,
                                                @RequestParam(value = "tarea") String tarea,
                                                @RequestParam(value = "city") String city){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Article article = new Article();
        Category category = new Category();
        category.setId(type);
        article.setTitle(title);
        article.setSummary("摘要");
        article.setContent(tarea);
        article.setViewSize(article.getViewSize()+1);
        article.setCity(city);
        article.setUser(user);
        article.setCategory(category);
        article.setIsSticky(PostStickyEnum.NOT_STICKY_POST.getCode());
        articleService.saveArticle(article);
        return ResponseEntity.ok().body(new Response(true,"处理成功"));
    }



    //用户中心
    @GetMapping("/manage/articles")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")//制定角色权限才能操作方法
    public ModelAndView articleList(Model model,
                                    @RequestParam(value = "status",required = false,defaultValue = "all")String status,
                                    @RequestParam(value = "keywords",required = false,defaultValue = "")String keywords,
                                    @RequestParam(value = "async",required = false,defaultValue = "false")Boolean async,
                                    @RequestParam(value = "pageIndex",required = false,defaultValue = "1")int pageIndex,
                                    @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Sort sort = new Sort(Sort.Direction.DESC,"isSticky","id");

        PageRequest pageRequest = PageRequest.of(pageIndex - 1,pageSize,sort);
        String postStatus = "";

        //全部
        Page<Article> page = null;
        List<String> statusList = new ArrayList<>();
        if (PostStatusEnum.ALL_POST.getCode().equals(status)){
            statusList.add(PostStatusEnum.PUBLISH_POST.getCode());
            statusList.add(PostStatusEnum.DRAFT_POST.getCode());
            statusList.add(PostStatusEnum.PRIVATE_POST.getCode());
            postStatus = "all";
        }
        //已发布
        else if (PostStatusEnum.PUBLISH_POST.getCode().equals(status)){
            statusList.add(status);
            postStatus = "publish";
        }
        //草稿
        else if (PostStatusEnum.DRAFT_POST.getCode().equals(status)){
            statusList.add(status);
            postStatus = "draft";
        }
        //私密
        else if (PostStatusEnum.PRIVATE_POST.getCode().equals(status)){
            statusList.add(status);
            postStatus = "private";
        }
        //其他
        else {
            statusList.add(PostStatusEnum.PUBLISH_POST.getCode());
            postStatus = "publsh";
        }
        page = articleService.listArticlesByUserAndStatusInAndKeywords(user, statusList, keywords, pageRequest);
        model.addAttribute("page",page);
        model.addAttribute("site_title",SiteTitleEnum.BLOG_MANAGE.getTitle());
        model.addAttribute("status",postStatus);
        model.addAttribute("keywords",keywords);
        return new ModelAndView();
    }

}
