/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.config;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Configuration
public class HttpMessageConverter {

  @Primary
  @Bean
  public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(Jackson2ObjectMapperBuilder builder) {
    MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
    ObjectMapper objectMapper = builder.createXmlMapper(false).build();

    // 配置json属性映射对象属性 不需要使用在属性中加@JsonProperty
    objectMapper.registerModule(new ParameterNamesModule());
    // Some other custom configuration for supporting Java 8 features
    objectMapper.registerModule(new Jdk8Module());
    // 配置时间 LocalDateTime 与 OffsetDateTime 序列化反序列化
    JavaTimeModule javaTimeModule = new JavaTimeModule();
    //设置序列化 反序列化
    javaTimeModule.addSerializer(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE);
    javaTimeModule.addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);
    javaTimeModule.addSerializer(OffsetDateTime.class, OffsetDateTimeSerializer.INSTANCE);
    javaTimeModule.addDeserializer(OffsetDateTime.class, InstantDeserializer.OFFSET_DATE_TIME);
    objectMapper.registerModule(javaTimeModule);

    // Use property 设置属性驼峰命名 不需要加下划线
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);

    // FAIL_ON_EMPTY_BEANS设置false表示 jackson序列化空对象(空bean)时，不抛异常
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    // 通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化
    // Include.Include.ALWAYS 默认
    // Include.NON_DEFAULT 属性为默认值不序列化
    // Include.NON_EMPTY 属性为 空（""） 或者为 NULL 都不序列化，则返回的json是没有这个字段的。这样对移动端会更省流量
    // Include.NON_NULL 属性为NULL 不序列化
    objectMapper.setSerializationInclusion(Include.ALWAYS);

    // 对象转JSON时，所有数字全转成字符串
    objectMapper.configure(Feature.WRITE_NUMBERS_AS_STRINGS, false);

    // 序列化日期时以timestamps输出，默认true
    // 比如如果一个类中有Date date;这种日期属性，序列化后为：{"date" : 1413800730456}
    // 若不为true，则为{"date" : "2014-10-20T10:26:06.604+0000"}
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    objectMapper.setTimeZone(TimeZone.getTimeZone(ZoneId.of("GMT+8")));

    jsonConverter.setObjectMapper(objectMapper);

    //设置中文编码格式 中文乱码解决方案
    List<MediaType> list = new ArrayList<MediaType>();
    //设定Json格式且编码为utf-8
    list.add(MediaType.APPLICATION_JSON_UTF8);
    jsonConverter.setSupportedMediaTypes(list);

    return jsonConverter;
  }
}
