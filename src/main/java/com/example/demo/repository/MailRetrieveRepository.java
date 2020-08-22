package com.example.demo.repository;

import com.example.demo.entity.MailRetrieve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRetrieveRepository extends JpaRepository<MailRetrieve,Long> {
    /**
     *根据账号查找记录
     */
    MailRetrieve findByAccount(String account);

}
