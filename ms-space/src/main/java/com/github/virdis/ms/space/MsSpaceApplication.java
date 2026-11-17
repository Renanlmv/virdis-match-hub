package com.github.virdis.ms.space;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsSpaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsSpaceApplication.class, args);
	}

}
