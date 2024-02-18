package com.example.order;

import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

@SpringBootApplication
public class OrderApplication {

	@Autowired
	EurekaClient eurekaClient;

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}

}
