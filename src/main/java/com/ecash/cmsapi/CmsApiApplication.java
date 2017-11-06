package com.ecash.cmsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.ecash.ecashcore", "com.ecash.cmsapi" })
@EntityScan(basePackages = { "com.ecash.ecashcore.model" })
@EnableJpaRepositories(basePackages = { "com.ecash.ecashcore.repository" })
public class CmsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CmsApiApplication.class, args);
	}
}
