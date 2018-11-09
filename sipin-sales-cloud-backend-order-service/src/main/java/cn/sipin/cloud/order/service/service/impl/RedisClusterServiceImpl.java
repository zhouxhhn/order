/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.sipin.cloud.order.service.service.RedisClusterServiceContract;
import cn.sipin.cloud.order.service.util.JsonTransformer;

@Primary
@Service
public class RedisClusterServiceImpl implements RedisClusterServiceContract {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  @Override public String get(String key) {
    return (String) redisTemplate.opsForValue().get(key);
  }

  @Override public String set(String key, String value) {
    redisTemplate.opsForValue().set(key, value);

    return get(key);
  }

  @Override public String set(String key, String value, int second) {
    redisTemplate.opsForValue().set(key, value, second, TimeUnit.MINUTES);

    return get(key);
  }

  @Override public Boolean setnx(String key, Object value) {
    return redisTemplate.opsForValue().setIfAbsent(key, value);
  }


  @Override public String setObject(String key, Object value) {
    return null;
  }

  @Override public String hget(String key, String field) {
    return (String) redisTemplate.opsForHash().get(key, field);
  }

  /**
   * HSET key field value
   */
  @Override public String hset(String key, String field, String value) {
    redisTemplate.opsForHash().put(key, field, value);
    return get(key);
  }

  @Override public List<Object> hmget(String key, Collection<Object> fields) {
    return redisTemplate.opsForHash().multiGet(key, fields);
  }

  @Override public void hmset(String key, Map<String, String> map) {

    redisTemplate.opsForHash().putAll(key, map);
  }

  @Override public String hsetObject(String key, String field, Object value) {

    redisTemplate.opsForHash().put(key, field, JsonTransformer.toJson(value));
    return get(key);
  }

  @Override public Boolean existKey(String key) {
    return redisTemplate.hasKey(key);
  }



  @Override public Long incr(String key) {
    return redisTemplate.opsForValue().increment(key, 1L);
  }

  @Override public Boolean expire(String key, int second) {
    return redisTemplate.expire(key, second, TimeUnit.MINUTES);
  }

  @Override public Long ttl(String key) {
    return redisTemplate.getExpire(key);
  }

  @Override public Boolean del(String key) {
    return redisTemplate.delete(key);
  }

  @Override public Long hdel(String key, String field) {
    return redisTemplate.opsForHash().delete(key, field);
  }
}
