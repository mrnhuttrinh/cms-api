package com.ecash.cmsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableSpringDataWebSupport
@EnableScheduling
@ComponentScan(basePackages = {
    "com.ecash.ecashcore" }, basePackageClasses = CmsApiApplication.class)
@EntityScan(basePackages = { "com.ecash.ecashcore.model" })
@EnableJpaRepositories(basePackages = { "com.ecash.ecashcore.repository" })
public class CmsApiApplication
{

  public static void main(String[] args)
  {
    SpringApplication.run(CmsApiApplication.class, args);
  }
}
