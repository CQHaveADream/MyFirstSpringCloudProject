package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
/*
* 该服务为注册中心，提供注册发现功能。当有服务接入时，会接受该服务的注册，类似于一个服务注册工厂。
* 该注册中心接收的服务注册有：
* 1.service-my
* 2.service-you
* 3.config-server
* 4.service-zuul
* */
@EnableEurekaServer
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
