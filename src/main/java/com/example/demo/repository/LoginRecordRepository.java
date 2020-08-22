package com.example.demo.repository;

import com.example.demo.entity.LoginRecord;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LoginRecordRepository extends JpaRepository<LoginRecord,Long> {

    List<LoginRecord> findTop50ByUserOrderByIdDesc(User user);

    Page<LoginRecord> findByUser(User user, Pageable pageable);
}
