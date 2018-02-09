package com.example.demo.Swagger2;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class testSwagger2 {

    @Bean
    public Docket CreateRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo.controller"))
                .paths(paths())
                .build()
                .useDefaultResponseMessages(false);
    }

    private Predicate<String> paths(){
        return Predicates.and(PathSelectors.regex("/.*"), Predicates.not(PathSelectors.regex("/error")));
    }

    //构建 api文档的详细信息函数
    private ApiInfo apiInfo(){
        Contact contact = new Contact("CQ", "https://gitee.com/CQHaveADream", "CQ18396821586@163.com");
        return new ApiInfoBuilder()
                .title("MyFirstSwagger")
                .description("First")
                .contact(contact)
                .license("Apache License Version 2.0")
                .termsOfServiceUrl("http://local:8762")
                .version("2.0")
                .build();
    }

}
