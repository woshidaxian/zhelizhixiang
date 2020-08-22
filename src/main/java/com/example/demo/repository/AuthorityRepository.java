package com.example.demo.repository;

import com.example.demo.entity.Authority;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority,Integer> {

    @Query(value = "SELECT user_id FROM user_authority where authority_id = ?", nativeQuery = true)
    List<Integer> listadministrator(int id);

}
