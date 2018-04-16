package com.ecash.cmsapi.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class ConverterRestConfigurerAdapter extends RepositoryRestConfigurerAdapter {

  @Bean
  StringToDateConverter stringToDateConverter() {
    return new StringToDateConverter();
  }
  
  @Autowired
  StringToDateConverter stringToDateConverter;
  
  @Override
  public void configureConversionService(ConfigurableConversionService conversionService) {
    conversionService.addConverter(stringToDateConverter);
  }
}
