package com.ecash.cmsapi.redis;

public interface CacheService {
  public void sadd(String key, String value);

  public void srem(String key, String value);

  public boolean sismember(String key, String value);

  public String get(String key);

  public String set(String key, String value);

  public void del(String key);
}
