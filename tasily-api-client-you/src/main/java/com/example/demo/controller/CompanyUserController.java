package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.CompanyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/local")
public class CompanyUserController {

    @Autowired
    private CompanyUserService service;

    @Value("${server.port}")
    private String port;

    @RequestMapping(value = "/UserLogin.form",method = RequestMethod.POST)
    public JSONObject  findUserByName(@RequestBody JSONObject object)throws Exception{
        String code = object.getString("code");
        String password = object.getString("password");
        object = service.Login(code,password);
        object.put("port",port);
        object.put("code",204);
        return object;
    }

    @RequestMapping("/hi")
    public String test1(){
        return "hi";
    }

}
