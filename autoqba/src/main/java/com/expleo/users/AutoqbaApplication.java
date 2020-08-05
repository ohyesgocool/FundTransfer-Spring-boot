package com.expleo.users;

import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableEurekaClient
@Configuration
public class AutoqbaApplication {


	public static void main(String[] args) throws IOException {
		SpringApplication.run(AutoqbaApplication.class, args);
	}
	
	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	   public Docket productApi() {
	      return new Docket(DocumentationType.SWAGGER_2).select()
	         .apis(RequestHandlerSelectors.basePackage("com.expleo.users")).build().apiInfo(apiEndPointsInfo())
	         .securitySchemes(Lists.newArrayList(apiKey()));
	   }
	
	private ApiKey apiKey() {    
	    return new ApiKey("Authorization", "Authorization", "header"); 
	}
	
	public static final Contact DEFAULT_CONTACT = new Contact(
		      "Expleo Solutions", "http://expleogroup.com/", "testmail@expleogroup.com");
	
	private ApiInfo apiEndPointsInfo() {
	        return new ApiInfoBuilder().title("Swagger Expleo")
	        	.description("Sample server of banking application using RESTfull service. For this sample, you can set the api key  (Bearer (your JWT token))  to test the authorized service."  )
	            .license("Export YAML")
	            .licenseUrl("http://localhost:8080/users/getyml")
	        	.version("1.0.0")
	        	 .contact(DEFAULT_CONTACT)
	            .build();
	    }
	
	 }

