/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "spring.redis.cluster")
@Data
public class RedisClusterConfig {

  private List<String> nodes;


  @Bean
  public LettuceConnectionFactory lettuceConnectionFactory() {

    return new LettuceConnectionFactory(new RedisClusterConfiguration(nodes));
  }


  @Bean
  RedisTemplate<String, Object> lettuceRedisTemplate() {


    // 设置序列化
    Jackson2JsonRedisSerializer<String> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<String>(String.class);
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.enableDefaultTyping(DefaultTyping.NON_FINAL);
    jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

    // 配置redisTemplate
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
    redisTemplate.setConnectionFactory(lettuceConnectionFactory());
    RedisSerializer<?> stringSerializer = new StringRedisSerializer();
    // key 序列化
    redisTemplate.setKeySerializer(stringSerializer);
    // value 序列化
    redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

    // Hash Key序列化
    redisTemplate.setHashKeySerializer(stringSerializer);
    // Hash Value 序列化
    redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
    // 初始化赋值
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }
}
