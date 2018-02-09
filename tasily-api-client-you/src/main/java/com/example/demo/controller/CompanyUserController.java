package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.CompanyUser;
import com.example.demo.service.CompanyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("local")
public class CompanyUserController {

    @Autowired
    private CompanyUserService service;

    @Value("${server.port}")
    private String port;
    @RequestMapping(value = "/findUserByName.form",method = RequestMethod.POST)
    public JSONObject  findUserByName(){
        JSONObject object = new JSONObject();
        object.put("Msg",service.findByName("陈前"));
        object.put("port",port);
        return object;
    }
}
