package com.example.demo.repository;

import com.example.demo.entity.Relationship;

import com.example.demo.entity.RelationshipPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, RelationshipPK> {
    /**
     * 根据关注者id查找所有记录（查找关注的人的id）
     * @param fromUserId
     * @return
     */
    @Query(value = "select to_user_id from relationship where from_user_id = ?1",nativeQuery = true)
    List<Integer> findByFromUserId(Integer fromUserId);

    /**
     * 根据被关注者查找所有记录(查找粉丝的id)
     * @param toUserId
     * @return
     */
    @Query(value = "select  from_user_id from relationship where to_user_id =?1",nativeQuery = true)
    List<Integer> findByToUserId(Integer toUserId);

    /**
     * 查询该用户的互相关注id
     * @ param userId
     * @ return
     */
    @Query(value = "SELECT distinct t1.from_user_id from  (SELECT * FROM relationship where to_user_id = ?1) AS t1 INNER JOIN relationship t2 ON t1.from_user_id = t2.to_user_id", nativeQuery = true)
    List<Integer> findFriendsByUserId(Integer userId);

    /**
     * 查询关注数
     * @param fromUserId
     * @return
     */
    Long countByFromUserId(Integer fromUserId);

    /**
     * 查询粉丝群
     * @param toUserId
     * @return
     */
    Long countByToUserId(Integer toUserId);

    /**
     * 判断一个用户是否关注了另一个用户
     * @param fromUserId
     * @param toUserId
     * @return
     */
    Relationship findByFromUserIdAndToUserId(Integer fromUserId,Integer toUserId);

    /**
     * 查询一个用户是否被一个用户关注
     * @param toUserId
     * @param fromUserId
     * @return
     */
    Relationship findByToUserIdAndFromUserId(Integer toUserId,Integer fromUserId);
}
