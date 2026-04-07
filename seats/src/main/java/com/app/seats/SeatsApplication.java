package com.app.seats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SeatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeatsApplication.class, args);
	}

}
