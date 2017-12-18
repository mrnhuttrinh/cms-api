package com.ecash.cmsapi.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

  @Value("${redis.host}")
  private String redisHost;

  @Value("${redis.port}")
  private String redisPort;

  @Bean(name = "redisClient")
  public JedisPool redisClient() {
    return new JedisPool(new JedisPoolConfig(), redisHost, Integer.parseInt(redisPort));
  }
}
