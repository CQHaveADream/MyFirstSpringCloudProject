package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@EnableHystrixDashboard //注解开启仪表盘的支持
//@EnableCircuitBreaker  //开启断路器
@EnableDiscoveryClient
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	@LoadBalanced //表明这个restRestTemplate开启负载均衡的功能
	RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Primary
	@Bean
	RestTemplate commRestTemplate() {
		return new RestTemplate();
	}

/*
* 启用基于OAuth2 的单点登录,做一些安全配置，
* 如果WebSecurityConfigurerAdapter类上注释了@EnableOAuth2Sso注解，
* 那么将会添加身份验证过滤器和身份验证入口
* 如果只有一个@EnableOAuth2Sso注解没有编写在WebSecurityConfigurerAdapter上，
* 那么它将会为所有路径启用安全，并且会在基于HTTP Basic认证的安全链之前被添加
*
*/
	/*@Component
	@EnableOAuth2Sso
	public static class OAuth2SecurityConfiguration extends WebSecurityConfigurerAdapter {

		@Override
		public void configure(HttpSecurity http) throws Exception {
			// 所有请求都得经过认证和授权
			http.antMatcher("/**")
					.authorizeRequests().anyRequest().authenticated()
					.and().authorizeRequests().antMatchers("/","/anon").permitAll()
					.and()
					// 这里之所以要禁用csrf，是为了方便。
					// 否则，退出链接必须要发送一个post请求，请求还得带csrf token
					// 那样我还得写一个界面，发送post请求
					.csrf().disable()
					// 退出的URL是/logout
					.logout().logoutUrl("/logout").permitAll()
					// 退出成功后，跳转到/路径。
					.logoutSuccessUrl("/");
		}

	}*/



}
