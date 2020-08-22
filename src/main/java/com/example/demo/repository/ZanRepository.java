package com.example.demo.repository;

import com.example.demo.entity.Zan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZanRepository extends JpaRepository<Zan, Long> {
}
