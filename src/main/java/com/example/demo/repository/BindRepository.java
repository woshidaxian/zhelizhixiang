package com.example.demo.repository;

import com.example.demo.entity.Bind;
import com.example.demo.entity.BindType;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BindRepository extends JpaRepository<Bind,Long> {
    /**
     * 根据绑定类型和识别码来确定绑定
     * @param bindType
     * @param identifier
     * @return
     */
    Bind findByBindTypeAndIdentifier(BindType bindType, String identifier);

    /**
     * 根据用户获得绑定列表
     * @param user
     * @return
     */
    List<Bind> findByUser(User user);


    Integer deleteByUser(User user);
}
