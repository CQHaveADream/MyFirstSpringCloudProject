package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.CompanyUserDao;
import com.example.demo.domain.CompanyUser;
import com.example.demo.util.JsonUtil;
import com.example.demo.util.RedisUtil;
import com.example.demo.util.RegularKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CompanyUserService {

    @Autowired
    private CompanyUserDao dao;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private RegularKey regularKey;

    //模拟登录
    //@CachePut 应用到写数据的方法上，如新增/修改方法，调用方法时会自动把相应的数据放入缓存
    //@CachePut(value = "addCache", keyGenerator = "wiselyKeyGenerator")
    public JSONObject Login(String code, String password) throws InterruptedException{

        CompanyUser user = dao.findByCode(code);
        //从Redis中读取
        if (redisUtil.hasKey(code)){
            String RdsPass = (String) redisUtil.getHashValue(code,"RdsPass");
            if (!password.equalsIgnoreCase(RdsPass)||password.equals("")||password==null){
                return jsonUtil.failure(404,"密码不正确","请重新输入密码");
            }
            return jsonUtil.ObjectToJSONObject(user);
        }
        Thread.sleep(1000);

        if (user==null||code==null||code.equals("")){
            return jsonUtil.failure(404,"用户信息不存在","请核实用户名信息");
        }
        //从数据库中获取密码
        String RegPas = regularKey.getBase64Decode(user.getPassword());

        if (!password.equalsIgnoreCase(RegPas)||password.equals("")||password==null){
            return jsonUtil.failure(404,"密码不正确","请重新输入密码");
        }

        //将信息存入Redis
        Map<String,Object> map = new HashMap<>();
        map.put("userName",user.getName());
        map.put("code",code);
        map.put("RdsPass",RegPas);
        redisUtil.PutValueForHash(code,map);
        return jsonUtil.ObjectToJSONObject(user);
    }

}
