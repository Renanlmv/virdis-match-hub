package com.github.virdis.ms.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class MsProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(
				MsProjectApplication.class,
				args
		);
	}
}