/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 订单生产服务应用主入口
 */
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(scanBasePackages = {"cn.sipin.cloud.order.service.*","cn.siyue.platform.*"})
@MapperScan("cn.sipin.cloud.order.service.mapper*")
public class OrderServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(OrderServerApplication.class);
  }
}
