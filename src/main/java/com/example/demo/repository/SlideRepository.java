package com.example.demo.repository;

import com.example.demo.entity.Slide;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlideRepository extends JpaRepository<Slide, Integer> {

    List<Slide> findByStatus(String status, Sort sort);

}
