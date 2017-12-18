package com.ecash.cmsapi.redis;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service("RedisService")
public class RedisService implements CacheService {

  @Resource(name = "redisClient")
  private JedisPool pool;

  public void sadd(String key, String value) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      jedis.sadd(key, value);
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public void srem(String key, String value) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      jedis.srem(key, value);
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public boolean sismember(String key, String value) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      return jedis.sismember(key, value);
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public String get(String key) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      return jedis.get(key);
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public String set(String key, String value) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      return jedis.set(key, value);
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }

  public void del(String key) {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
      jedis.del(key);
    } finally {
      if (jedis != null) {
        jedis.close();
      }
    }
  }
}
