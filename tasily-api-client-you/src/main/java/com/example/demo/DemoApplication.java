package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
/*
* 当有外部接口通过路由网关调用本服务中的接口时，会开启负载均衡
* */
@EnableDiscoveryClient
@SpringBootApplication
public class DemoApplication {

	@Bean
	@LoadBalanced //表明这个restRestTemplate开启负载均衡的功能
	RestTemplate restTemplate(){
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
