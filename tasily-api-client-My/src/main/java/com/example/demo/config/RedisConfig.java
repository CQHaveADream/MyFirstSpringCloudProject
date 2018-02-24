package com.example.demo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/*
* Redis 的配置类
* */

@Component
public class RedisConfig {

    @Autowired
    private RedisConnectionFactory factory;

    @Bean
    public RedisTemplate<String,Object> redisTemplate(){
        RedisTemplate<String,Object> template = new RedisTemplate<>();
        initDomainRedisTemplate(template,factory);
        return template;
    }

    /*
*  自定义key生成策略
 * 类名+方法名+参数(适用于分布式缓存)，
 * 默认key生成策略分布式下有可能重复被覆盖
* */
    /*@Bean
    public static KeyGenerator wiselyKeyGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(target.getClass().getName());
                buffer.append(method.getName());
                for(Object obj:params){
                    buffer.append(obj);
                }
                return buffer.toString();
            }
        };
    }*/

    //序列化方式
    public void initDomainRedisTemplate(RedisTemplate template,RedisConnectionFactory factory){
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);
        template.setValueSerializer(serializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
    }

    @Bean
    public HashOperations<String,String,Object> hashOperations (RedisTemplate<String,Object> template){
        return template.opsForHash();
    }

    @Bean
    public SetOperations<String,Object> setOperations (RedisTemplate<String,Object> template){
        return template.opsForSet();
    }

    @Bean
    public ListOperations<String,Object> listOperations(RedisTemplate<String,Object> redisTemplate){
        return redisTemplate.opsForList();
    }

    @Bean
    public ZSetOperations<String,Object> zSetOperations(RedisTemplate<String,Object> redisTemplate){
        return redisTemplate.opsForZSet();
    }



}
