package com.example.demo.repository;


import com.example.demo.entity.SelectedArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectedArticleRepository extends JpaRepository<SelectedArticle,Long> {

}
