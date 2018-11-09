/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.sipin.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


import lombok.Data;

/**
 * 斯品商城配置
 *
 * @author Sipin ERP Development Team
 */
@Component
@Primary
@ConfigurationProperties(prefix = "thirdparty.sipin")
@Data
public class SipinProperties {
  /**
   * App token
   */
  private String token;

  /**
   * 同步 host 地址
   */
  private String host;

  /**
   * 同步 IP 地址
   */
  private String ip;


  private Client client;

  @Data
  public static class Client {

    private Long connectTimeout;

    private Long writeTimeout;

    private Long readTimeout;
  }

}
