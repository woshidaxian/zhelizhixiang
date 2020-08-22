package com.example.demo.service.impl;

import com.example.demo.entity.Article;
import com.example.demo.entity.Bookmark;
import com.example.demo.entity.User;
import com.example.demo.enums.ReputationEnum;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.BookmarkRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class BookmarkServiceImpl implements BookmarkService {
    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<Bookmark> findBookmarkByUser(User user, Pageable pageable) {
        return bookmarkRepository.findDistinctByUser(user, pageable);
    }

    @Override
    public Bookmark findById(Long id) {
        return bookmarkRepository.findById(id).get();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveBookmark(Bookmark bookmark) {
        //添加书签
        bookmarkRepository.save(bookmark);
        //给文章增加收藏数
        Article originalArticle = bookmark.getArticle();
        originalArticle.setBookmarkSize(originalArticle.getBookmarkSize() + 1);
        articleRepository.save(originalArticle);

        //2、修改用户积分
        User originalUser = originalArticle.getUser();
        Integer reputation = originalUser.getReputation();
        originalUser.setReputation(reputation + ReputationEnum.ARTICLE_GET_BOOKMARK.getCode());
        userRepository.save(originalUser);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBookmark(Bookmark bookmark) {
        //删除书签
        bookmarkRepository.delete(bookmark);
        //给文章减少收藏数
        Article originalArticle = bookmark.getArticle();
        Integer count = originalArticle.getBookmarkSize();
        if (count > 0) {
            originalArticle.setBookmarkSize(count - 1);
            articleRepository.save(originalArticle);
        }
    }

    @Override
    public Integer countByArticle(Article article) {
        return bookmarkRepository.countByArticle(article);
    }

    @Override
    public Boolean isMarkArticle(User user, Article article) {
        List<Bookmark> bookmarkList = bookmarkRepository.findByUserAndArticle(user, article);
        if (bookmarkList == null || bookmarkList.size() == 0) {
            return false;
        } else {
            return true;
        }
    }
}
