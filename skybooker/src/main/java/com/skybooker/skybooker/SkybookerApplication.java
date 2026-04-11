package com.skybooker.skybooker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class SkybookerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkybookerApplication.class, args);
	}

}
