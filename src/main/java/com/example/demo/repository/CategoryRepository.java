package com.example.demo.repository;

import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    /**
     * 根据用户查找分类，分页
     * @param user
     * @param pageable
     * @return
     */
    Page<Category> findByUserOrderByPositionAsc(User user, Pageable pageable);

    /**
     * 根据用户查找用户，不分页
     * @param user
     * @return
     */
    List<Category> findByUserOrderByPositionAsc(User user);

    /**
     * 根据用户和分类来查找
     * @param user
     * @param name
     * @return
     */
    List<Category> findByUserAndName(User user,String name);

    /**
     * 根据用户名和分类id查找
     * @param user
     * @param id
     * @return
     */
    Category findByUserAndId(User user,Long id);

    @Query(value = "SELECT  max (position) from category where user_id =?1",nativeQuery = true)
    Integer getMaxPosition(Integer userId);

    Integer deleteByUser(User user);

}
