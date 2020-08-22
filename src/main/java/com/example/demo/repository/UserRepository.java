package com.example.demo.repository;

import com.example.demo.entity.Authority;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {

    /**
     * 获得所有用户
     *
     * @param pageable
     * @return
     */

    Page<User> findAll(Pageable pageable);

    Page<User> findByAuthoritiesContainsAndUsernameLikeOrAuthoritiesContainsAndNicknameLike(List<Authority> authorityList, String username, List<Authority> authorityList2, String nickname, Pageable pageable);

    /**
     * 根据昵称分页查询用户列表
     *
     * @param name
     * @param pageable
     * @return
     */
    Page<User> findByNicknameLike(String name, Pageable pageable);


    /**
     * 根据用户名查询
     *
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 根据用户名列表查询
     *
     * @param usernames
     * @return
     */
    List<User> findByUsernameIn(Collection<String> usernames);

    /**
     * 根据Email查找
     *
     * @param email
     * @return
     */
    User findByEmail(String email);

    /**
     * 根据用户名和Email查找
     *
     * @param username
     * @param email
     * @return
     */
    User findByUsernameAndEmail(String username, String email);


    /**
     * 根据id集合查询用户，分页查询
     *
     * @param ids
     * @return
     */
    Page<User> findByIdIn(List<Integer> ids, Pageable pageable);


    /**
     * 统计某个状态的用户数
     *
     * @param authorityList
     * @return
     */
    Integer countByAuthoritiesContains(List<Authority> authorityList);


    //根据用户声望排名
    @Query(value = "SELECT * FROM user WHERE status = 'normal' ORDER BY reputation DESC LIMIT 10", nativeQuery = true)
    List<User> getUserRankByReputation();


}
