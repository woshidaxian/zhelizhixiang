package com.example.demo.service.impl;

import com.example.demo.VO.HotArticleVO;
import com.example.demo.VO.SelectedArticleVO;
import com.example.demo.entity.Article;
import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import com.example.demo.entity.Zan;
import com.example.demo.enums.PostStatusEnum;
import com.example.demo.enums.ReputationEnum;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.ArticleService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    @Override
    public Page<Article> listArticle(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

    @Override
    public List<Article> listArticle() {
        return articleRepository.findAll();
    }

    @Override
    public Page<Article> listArticleByStatusIn(List<String> statusList, Pageable pageable) {
        return articleRepository.findByStatusIn(statusList,pageable);
    }

    @Override
    public List<Article> listArticleByStatusIn(List<String> status) {
        return articleRepository.findByStatusIn(status);
    }

    @Override
    public Page<Article> listArticleByUserAndStatusIn(List<String> statusList, Pageable pageable,User user) {
        return articleRepository.findByUserAndStatusIn(user,statusList,pageable);
    }

    @Override
    public List<Article> listArticleByUserAndStatusIn(List<String> status, User user) {
        return articleRepository.findByUserAndStatusIn(status,user);
    }

    @Override
    public Page<Article> listArticlesByStatusInAndKeywords(List<String> statusList, String keywords, Pageable pageable) {
        return articleRepository.findByStatusInAndTitleLikeOrStatusInAndTagsLike(statusList, "%" + keywords + "%", statusList, "%" + keywords + "%", pageable);
    }


    @Override
    public Page<Article> listArticlesByKeywords(String keywords,Pageable pageable) {
        return articleRepository.findByTitleLikeOrTagsLike("%" + keywords + "%","%" + keywords + "%",pageable);
    }

    @Override
    public Page<Article> listArticlesByUserAndKeywords(String keywords, User user, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Article> listArticlesByUserAndStatusInAndKeywords(User user, List<String> statusList, String keywords, Pageable pageable) {
        //用户主页搜索，根据内容查找
        return articleRepository.findByUserAndStatusInAndContentLike(user,statusList,"%" + keywords +"%",pageable);
    }

    @Override
    public Page<Article> listArticlesByStickyAndStatusIn(User user, Integer sticky, List<String> status, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Article> listArticleByCategoryAndStatusInAndKeywords(Category category, List<String> status, String keywords, Pageable pageable) {
        return null;
    }

    @Override
    public Article saveArticle(Article article) {
        if (article.getId() == null){
            //1、添加文章
            Article returnArticle = articleRepository.save(article);
            returnArticle.setGuid("/articles/" + returnArticle.getId());
            //2、修改文章数和积分
            User originalUser = userRepository.findById(article.getUser().getId()).get();
            Long count = articleRepository.countArticleSizeByUserId(article.getUser().getId());
            Integer reputation = originalUser.getReputation();
            originalUser.setReputation(reputation + ReputationEnum.PUBLISH_ARTICLE.getCode());
            originalUser.setArticleSize(count);
            userRepository.save(originalUser);
            return returnArticle;
        }else {
            //修改文章
            return articleRepository.save(article);
        }
    }

    @Override
    public boolean removeArticle(Article article) {
        //如果文章状态不是已删除，则先放到回收站。
        if (Objects.equals(PostStatusEnum.DELETED_POST.getCode(),article.getId())){
            //删除文章对应的评论，再删除一级评论
            commentRepository.deleteByArticle(article);
            articleRepository.delete(article);
        }else {
            //标记删除
            article.setStatus(PostStatusEnum.DELETED_POST.getCode());
            articleRepository.save(article);
            //修改文章数和积分
            Long count = articleRepository.countArticleSizeByUserId(article.getUser().getId());
            User originalUser = article.getUser();
            Integer reputation = originalUser.getReputation();
            originalUser.setReputation(reputation - ReputationEnum.PUBLISH_ARTICLE.getCode());
            originalUser.setArticleSize(count);
            userRepository.save(originalUser);
        }
        return true;
    }

    @Override
    public Article getArticleById(Long id) {
        return articleRepository.findById(id).get();
    }

    @Override
    public Article viewIncrease(Long id) {
        //添加当前文章的cookie
        Article  article = articleRepository.findById(id).get();
        if (article == null){
            return null;
        }
        article.setViewSize(article.getViewSize() + 1);
        this.saveArticle(article);
        return article;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createZan(Long articleId) {
        //添加赞
        Article originalArticle = articleRepository.findById(articleId).get();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user = userService.getUserByUsername(user.getUsername());
        Zan zan = new Zan(user);
        boolean isExist = originalArticle.addZan(zan);
        if (isExist){
            throw new IllegalArgumentException("您已经点过赞了!");
        }
        saveArticle(originalArticle);

        //修改用户积分
        User originalUser = originalArticle.getUser();
        Integer reputation = originalUser.getReputation();
        originalUser.setReputation(reputation + ReputationEnum.ARTICLE_GET_ZAN.getCode());
        userRepository.save(originalUser);

    }

    //热门文章
    @Override
   // @Cacheable(value = "1h",key = "'article:top10hot'")
    public List<HotArticleVO> listTop10HotArticles() {
        List<HotArticleVO> hotArticleVOList = new ArrayList<>();
        List<Article> articleList = articleRepository.findTop10HotArticle();
        for (int i = 0; i < articleList.size();i++){
            Article article = articleList.get(i);
            HotArticleVO hotArticleVO = new HotArticleVO();
            hotArticleVO.setId(article.getId());
            hotArticleVO.setTitle(article.getTitle());
            hotArticleVO.setCommentSize(article.getCommentSize());
            hotArticleVO.setGuid(article.getGuid());
            hotArticleVOList.add(hotArticleVO);
        }
        return hotArticleVOList;
    }

    @Override
    public List<Article> listArticles() {
        List<Article> articleList = articleRepository.findAll();
        return articleList;
    }

    //精选文章
    @Override
    public List<SelectedArticleVO> SeleteHotArticles() {
        List<SelectedArticleVO> selectedArticleList = new ArrayList<>();
        List<Article> articleList = articleRepository.findTop10HotArticle();
        for (int i = 0; i < articleList.size(); i ++){
            Article article = articleList.get(i);
            SelectedArticleVO selectedArticleVO = new SelectedArticleVO();
            selectedArticleVO.setGuid(article.getGuid());
            selectedArticleVO.setCreateTime(article.getCreateTime());
            selectedArticleVO.setId(article.getId());
            selectedArticleVO.setName(article.getUser().getNickname());
            selectedArticleVO.setTitle(article.getTitle());
            selectedArticleVO.setStatus(article.getStatus());
            selectedArticleList.add(selectedArticleVO);
        }
        return selectedArticleList;
    }

    @Override
    public Long countArticleByStatus(String status) {
        return articleRepository.countByStatus(status);
    }

    @Override
    public Page<Article> listArticlesByTag(String tagName, Pageable pageable) {
        return articleRepository.findByTagsLike("%" + tagName + "%",pageable);
    }

    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void deleteByStatus(String status) {
        Integer result = articleRepository.deleteByStatus(status);
        System.out.println(result);
    }

    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void batchRemove(List<Long> ids) {
        for (int i = 0; i < ids.size(); i++){
            Article article = articleRepository.findById(ids.get(i)).get();
            if (article != null){
                //删除文章
                articleRepository.deleteById(ids.get(i));
                //删除对应的评论
                commentRepository.deleteByArticle(article);
            }
        }
    }
}
