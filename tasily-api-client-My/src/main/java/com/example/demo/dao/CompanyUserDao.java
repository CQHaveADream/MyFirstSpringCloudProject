package com.example.demo.dao;

import com.example.demo.domain.CompanyUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

public interface CompanyUserDao extends JpaRepository<CompanyUser,Serializable>{

    @Query("select u from CompanyUser u where u.code = ?1")
    CompanyUser findByCode(String code);

    @Transactional
    @Modifying
    @Query("delete from CompanyUser u where u.code = ?1")
    void deleteByCode(String code);

    @Override
    Page<CompanyUser> findAll(Pageable pageable);
}
