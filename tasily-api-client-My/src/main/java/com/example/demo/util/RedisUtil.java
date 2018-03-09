package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*
* Redis 的工具类
*
* */

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private HashOperations<String,String,Object> hashOperations;

    @Autowired
    private SetOperations<String,Object> setOperations;

    @Autowired
    private ListOperations<String,Object> listOperations;

    @Autowired
    private ZSetOperations<String,Object> zSetOperations;

     /*---------------------------------------------公共区域------------------------------------------------------------*/

    //判断key是否存在
    public boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    //根据key删除key和value
    public void deleteByKey(String key){
        redisTemplate.delete(key);
    }

    /*
    * 设置key的有效期
    * @param key,time(有效时间),unit(hours,days,minutes)
    * */
    public void setExpire(String key,long time,TimeUnit unit){
        if (time != -1){
            redisTemplate.expire(key,time,unit);
        }
    }

    /*---------------------------------------------公共区域------------------------------------------------------------*/


 /*----------------------------------------------String---------------------------------------------------------*/

    /**
     * 插入数据
     * @param key,value
     * @return
     */
    public void addForInt(String key, int value){
        redisTemplate.opsForValue().set(key,value);
    }

    /*
   * 根据key值，累加value
   * @param  key,num
   * */
    public double Count(String key, double num){
        return redisTemplate.opsForValue().increment(key,num);
    }

    //根据key获取value值
    public String getStringValue(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }

    public int getIntValue(String key){
        return (int)redisTemplate.opsForValue().get(key);
    }

    //插入String类型的value
    public void setValue(String key,String value){
        redisTemplate.opsForValue().set(key,value);
    }



    /*----------------------------------------------String---------------------------------------------------------*/


    /*----------------------------------------------Hash---------------------------------------------------------*/

    public void PutValueForHash(String key, Map<String,Object> map){
        hashOperations.putAll(key,map);
    }

    public boolean hasHashKey(String key,Object hashKey){
       return hashOperations.hasKey(key,hashKey);
    }

    public List<Object> getAllValueForHash(String key){
        return hashOperations.values(key);
    }

    public Object getHashValue(String key,Object hashKey){
       return hashOperations.get(key,hashKey);
    }
}
