/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 订单消费服务应用主入口
 */
@EnableCircuitBreaker
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"cn.sipin.cloud.order.client.*","cn.siyue.platform.*"})
public class OrderClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(OrderClientApplication.class);
  }
}
