package com.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.CompanyUserDao;
import com.example.demo.dao.RsaDao;
import com.example.demo.domain.CompanyUser;
import com.example.demo.domain.RSAKey;
import com.example.demo.util.JsonUtil;
import com.example.demo.util.RSAUtil;
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
    private RSAUtil rsaUtil;

    @Autowired
    private RsaDao rsaDao;

    //模拟登录
    //@CachePut 应用到写数据的方法上，如新增/修改方法，调用方法时会自动把相应的数据放入缓存
    //@CachePut(value = "addCache", keyGenerator = "wiselyKeyGenerator")
    public JSONObject Login(String code, String password) throws Exception{

        //从Redis中读取
        if (redisUtil.hasKey(code)){
            String RdsPas = (String) redisUtil.getHashValue(code,"RdsPass");
            if (!password.equalsIgnoreCase(RdsPas)||password.equals("")||password==null){
                return jsonUtil.failure(404,"Password Not Correct","请重新输入密码");
            }
            return jsonUtil.success("Redis Have This User");
        }
        Thread.sleep(1000);

        CompanyUser user = dao.findByCode(code);
        if (user==null||code==null||code.equals("")){
            return jsonUtil.failure(404,"User Not Found","请核实用户名信息");
        }
        //从数据库中获取密码
        //String RegPas = regularKey.getBase64Decode(user.getPassword());
        RSAKey rsaKey = rsaDao.findRsaById(1);
        byte[] RegPas = rsaUtil.decryptByPrivateKey(rsaUtil.decryptBASE64(user.getPassword()),rsaKey.getPrivateKey());
        if (!password.equalsIgnoreCase(new String(RegPas,"utf-8"))||password.equals("")||password==null){
            return jsonUtil.failure(404,"密码不正确","请重新输入密码");
        }

        //将信息存入Redis
        Map<String,Object> map = new HashMap<>();
        map.put("userName",user.getName());
        map.put("code",code);
        map.put("RdsPass",new String (RegPas));
        redisUtil.PutValueForHash(code,map);
        return jsonUtil.ObjectToJSONObject(user);
    }

}
