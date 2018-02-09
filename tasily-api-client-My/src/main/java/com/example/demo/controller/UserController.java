package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.CompanyUser;
import com.example.demo.service.CompanyUserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CompanyUserService service;

    @Value("${server.port}")
    private String port;

    @Autowired
    private RestTemplate restTemplate;

    private String localUrlPrefix = "http://service-you";

    @RequestMapping(value = "/Register.form",method = RequestMethod.POST)
    public JSONObject Register(@RequestBody JSONObject code){
      return service.UserRegister(code);
    }

    @HystrixCommand(fallbackMethod = "Error") //如果出现异常，就去调用Error方法，防止出现雪崩效应
    @RequestMapping(value = "/toLogin.form",method = RequestMethod.POST)
    public Object toLogin(@RequestBody JSONObject Msg)throws InterruptedException{
        String code = Msg.getString("code");
        String password = Msg.getString("password");
        boolean flag = Msg.getBoolean("flag");
        if (flag){ //通过路由，去调用service-you服务中的findUserByName接口()
            String Url = localUrlPrefix + "/local/findUserByName.form";
            return restTemplate.postForObject(Url,CompanyUser.class,JSONObject.class);
        }
        JSONObject object = service.Login(code,password);
        object.put("port",port);
        return object;
    }

    @RequestMapping(value = "/UserCancellation.form",method = RequestMethod.POST)
    public JSONObject UserCancellation(@RequestBody JSONObject Msg) throws InterruptedException{
        String code = Msg.getString("code");
        return service.Cancellation(code);
    }

    //断路器
    public JSONObject Error(JSONObject error){
        error.put("error","请联系管理员，我们会尽快解决！");
        return error;
    }
}
