package com.example.fooddeliveryproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@SpringBootApplication
public class FooddeliveryprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(FooddeliveryprojectApplication.class, args);
	}

}
