package com.example.demo.dao;

import com.example.demo.domain.CompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

public interface CompanyUserDao extends JpaRepository<CompanyUser,Serializable> {

    @Query("select c from CompanyUser c where c.name = ?1")
    CompanyUser findByName(String code);
}
