package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.CompanyUser;
import com.example.demo.service.CompanyUserService;
import com.example.demo.util.JsonUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

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

    @Autowired
    private JsonUtil jsonUtil;

    @RequestMapping(value = "/Register.form",method = RequestMethod.POST)
    public JSONObject Register(@RequestBody JSONObject code){
      return service.UserRegister(code);
    }


   @HystrixCommand(fallbackMethod = "Error", commandProperties = {
            @HystrixProperty(name="execution.isolation.strategy", value = "THREAD"), //默认隔离策略 它在单独的线程上执行，并发请求受线程池中的线程数量的限制 一般用于网络调用
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "4000"), //超时时间
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), //熔断触发的最小个数/10s 默认20
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"), //失败率达到多少百分比后熔断
    }, threadPoolProperties = {
            @HystrixProperty(name = "coreSize", value = "10"), //线程池核心线程数 默认10
            @HystrixProperty(name = "maxQueueSize", value = "7"), //请求等待队列 默认-1
            @HystrixProperty(name = "keepAliveTimeMinutes", value = "1000"), //线程池中空闲线程生存时间 默认1
            @HystrixProperty(name = "queueSizeRejectionThreshold", value = "8"), //排队线程数量阈值，默认为5，达到时拒绝，如果配置了该选项，队列的大小是该队列
            @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "10"), //滚动统计窗口划分的桶数 默认10
            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1440") //statistical rolling窗口的持续时间（以毫秒为单位）。 这是为线程池保留多长时间
    })
    @RequestMapping(value = "/toLogin.form",method = RequestMethod.POST)
    @CrossOrigin //实现跨域
    public Object toLogin(@RequestBody JSONObject object)throws InterruptedException{
        String code = object.getString("code");
        String password = object.getString("password");
        //通过路由，去调用service-you服务中的/UserLogin.form接口(负载均衡)
        String Url = localUrlPrefix + "/local/UserLogin.form";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        JSONObject params = new JSONObject();
        params.put("code",code);
        params.put("password",password);
        HttpEntity<String> formEntity = new HttpEntity<>(params.toString(), headers);
        return restTemplate.postForObject(Url, formEntity, String.class);
    }

    @RequestMapping(value = "/UserCancellation.form",method = RequestMethod.POST)
    public JSONObject UserCancellation(@RequestBody JSONObject Msg) throws InterruptedException{
        String code = Msg.getString("code");
        return service.Cancellation(code);
    }

    @RequestMapping(value = "/findAllUser",method = RequestMethod.POST)
    public Page<CompanyUser> findAllUser(@RequestBody JSONObject code){
       return service.findAllUser(code);
    }

    //断路器回调函数
    public JSONObject Error(JSONObject error){
        error = jsonUtil.failure(500,"Login接口出现问题","请及时查看");
        return error;
    }
}
