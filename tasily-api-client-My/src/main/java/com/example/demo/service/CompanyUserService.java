package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.CompanyUserDao;
import com.example.demo.dao.RsaDao;
import com.example.demo.domain.CompanyUser;
import com.example.demo.domain.RSAKey;
import com.example.demo.util.JsonUtil;
import com.example.demo.util.RSAUtil;
import com.example.demo.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyUserService {

    @Autowired
    private CompanyUserDao userDao;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RSAUtil rsaUtil;

    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private RsaDao rsaDao;

    //模拟注册
    public JSONObject UserRegister(JSONObject msg)throws Exception{

        String name = msg.getString("name");
        String address = msg.getString("address");
        String code = msg.getString("code");
        String password = msg.getString("password").toLowerCase();
        String userName = userDao.findNameByCode(code);
        if (userName != null){
            return jsonUtil.failure(400,"用户名已经存在，请重新注册",null);
        }
        if (!password.matches("[a-zA-Z0-9]{9}")){
            return jsonUtil.failure(400,"密码长度为9位，由数字1-9和字母a-z(不区分大小写)组成",null);
        }
        RSAKey rsaKey = rsaDao.findRsaById(1);
        //得到用公钥加密后的password
        byte[] RsaPassword = rsaUtil.encryptByPublicKey(password.getBytes(), rsaKey.getPublicKey());
        //String key = rsaUtil.setBase64Encryptor(password);
        CompanyUser user = new CompanyUser();
        user.setName(name);
        user.setAddress(address);
        user.setCode(code);
        user.setPassword(rsaUtil.encryptBASE64(RsaPassword));
        userDao.save(user);
        return jsonUtil.success(null);
    }

    //注销用户
    @Transactional
    public JSONObject Cancellation(String code) throws InterruptedException{
        //如果Redis中有该信息则删除
        if (redisUtil.hasKey(code)){
            redisUtil.deleteByKey(code);
        }
        userDao.deleteByCode(code);
        return jsonUtil.success(null);
    }

    //根据用户名查找用户姓名
    public String findNameByCode(String code){
        CompanyUser user = userDao.findByCode(code);
        String name = user.getName();
        return name;
    }
    //查询所有用户
    public Page<CompanyUser> findAllUser(JSONObject code){
        int start = code.getIntValue("start");
        int end = code.getIntValue("end");
        return userDao.findAll(new PageRequest(start,end));
    }

}
