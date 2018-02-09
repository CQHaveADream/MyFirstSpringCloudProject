package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.CompanyUserDao;
import com.example.demo.domain.CompanyUser;
import com.example.demo.util.JsonUtil;
import com.example.demo.util.RedisUtil;
import com.example.demo.util.RegularKey;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class CompanyUserService {

    @Autowired
    private CompanyUserDao dao;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RegularKey regularKey;

    @Autowired
    private JsonUtil jsonUtil;

    //模拟注册
    public JSONObject UserRegister(JSONObject msg){

        String name = msg.getString("name");
        String address = msg.getString("address");
        String code = msg.getString("code");
        String password = msg.getString("password").toLowerCase();
        if (!password.matches("[a-zA-Z0-9]{9}")){
            return jsonUtil.failure(400,"密码长度为9位，由数字1-9和字母a-z(不区分大小写)组成",null);
        }
        String key = regularKey.setBase64Encryptor(password);

        CompanyUser user = new CompanyUser();
        user.setName(name);
        user.setAddress(address);
        user.setCode(code);
        user.setPassword(key);
        dao.save(user);
        return jsonUtil.success(null);
    }

    //模拟登录
    public JSONObject Login(String code,String password) throws InterruptedException{
        //从Redis中读取
        if (redisUtil.hasKey(code)){
            String RdsPass = redisUtil.getStringValue(code);
                if (!password.equalsIgnoreCase(RdsPass)||password.equals("")||password==null){
                    return jsonUtil.failure(404,"密码不正确","请重新输入密码");
                }
            return jsonUtil.success(null);
        }
        Thread.sleep(1000);

        CompanyUser user = dao.findByCode(code);
        if (user==null||code==null||code.equals("")){
            return jsonUtil.failure(404,"用户信息不存在","请核实用户名信息");
        }
        //从数据库中获取密码
        String RegPas = regularKey.getBase64Decode(user.getPassword());

        if (!password.equalsIgnoreCase(RegPas)||password.equals("")||password==null){
            return jsonUtil.failure(404,"密码不正确","请重新输入密码");
        }

        //将信息存入Redis
        redisUtil.setValue(code,RegPas);
        return jsonUtil.success(null);
    }

    //断路器
    public JSONObject FindError(JSONObject error){
         error.put("error","请联系管理员，我们会尽快处理！");
         return error;
    }

    //注销用户
    @Transactional
    public JSONObject Cancellation(String code) throws InterruptedException{
        //如果Redis中有该信息则删除
        if (redisUtil.hasKey(code)){
            redisUtil.deleteByKey(code);
        }

        dao.deleteByCode(code);
        return jsonUtil.success(null);
    }

}
