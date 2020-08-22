package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {
    Category getCategoryById(Long id);

    /**
     * 获得每个用户的分类列表
     * @param user
     * @return
     */
    List<Category> listCategorys(User user);

    /**
     * 获得每个用户的分类列表，分页显示
     * @param user
     * @param pageable
     * @return
     */
    Page<Category> listCategorys(User user, Pageable pageable);

    /**
     * 添加/跟新分类
     * @param category
     * @return
     */
    Category saveCategory(Category category);

    /**
     * 根据用户和分类名查询分类列表
     * @param user
     * @param name
     * @return
     */
    List<Category> listCategorysByUserAndName(User user,String name);

    /**
     * 删除分类
     * @param id
     */
    void removeCategory(Long id);

    /**
     * 向上移动
     * @param user
     * @param currentId
     * @param otherId
     */
    void changePriority(User user,Long currentId,Long otherId);

    /**
     * 根据分类的id和用户查找
     * @param user
     * @param id
     * @return
     */
    Category getCategoryByUserAndId(User user,Long id);
}
