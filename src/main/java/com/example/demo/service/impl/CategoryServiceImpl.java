package com.example.demo.service.impl;

import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public List<Category> listCategorys(User user) {
        return categoryRepository.findByUserOrderByPositionAsc(user);
    }

    @Override
    public Page<Category> listCategorys(User user, Pageable pageable) {
        return categoryRepository.findByUserOrderByPositionAsc(user,pageable);
    }

    @Transactional
    @Override
    public Category saveCategory(Category category) {
        //创建分类
        if (category.getId() == null){
            Integer maxPositioin = categoryRepository.getMaxPosition(category.getUser().getId());
            if (maxPositioin == null){
                maxPositioin = 1;
            }else {
                maxPositioin = categoryRepository.getMaxPosition(category.getUser().getId()) + 1;
            }
            category.setPosition(maxPositioin);
            Category returnCategory = categoryRepository.save(category);
            returnCategory.setGuid("/" + category.getUser().getUsername() + "/arricle?category=" + returnCategory.getId());
            return returnCategory;
        }else {
            //跟新分类
            return categoryRepository.save(category);
        }
    }

    @Override
    public List<Category> listCategorysByUserAndName(User user, String name) {
        return categoryRepository.findByUserAndName(user, name);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void changePriority(User user, Long currentId, Long otherId) {
        Category currentCategory = categoryRepository.findByUserAndId(user,currentId);//当前分类
        Category otherCategory = categoryRepository.findByUserAndId(user,otherId);//另一个分类
        Integer currentPriority = currentCategory.getPosition();//1
        Integer otherPriority = otherCategory.getPosition();//2
        //交换
        currentCategory.setPosition(otherPriority);
        otherCategory.setPosition(currentPriority);
        //写入数据库
        categoryRepository.save(currentCategory);
        categoryRepository.save(otherCategory);
    }

    @Override
    public Category getCategoryByUserAndId(User user, Long id) {
            return categoryRepository.findByUserAndId(user, id);
    }
}
