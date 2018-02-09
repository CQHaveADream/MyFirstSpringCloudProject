package com.example.demo.service;

import com.example.demo.dao.CompanyUserDao;
import com.example.demo.domain.CompanyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyUserService {

    @Autowired
    private CompanyUserDao dao;

    public CompanyUser findByName(String code){
       return dao.findByName(code);
    }
}
