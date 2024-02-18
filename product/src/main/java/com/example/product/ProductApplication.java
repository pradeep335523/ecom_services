package com.example.product;

import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductApplication {
	@Autowired
	EurekaClient eurekaClient;

	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}

}
