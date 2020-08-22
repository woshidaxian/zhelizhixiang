package com.example.demo.repository;

import com.example.demo.entity.NoticeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeTypeRepository extends JpaRepository<NoticeType,Integer> {
    List<NoticeType> findByIdInOrderById(List<Integer> ids);
}
