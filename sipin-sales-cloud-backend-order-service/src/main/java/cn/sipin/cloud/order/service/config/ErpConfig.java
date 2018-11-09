/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import cn.sipin.cloud.order.service.contract.Loggable;
import cn.sipin.cloud.order.service.feign.impl.SalesUserServiceImpl;
import cn.siyue.platform.constants.ResponseBackCode;
import cn.siyue.platform.exceptions.exception.RequestException;
import lombok.Data;

@Component
@Primary
@ConfigurationProperties(prefix = "erp.config")
@Data
public class ErpConfig implements Loggable {

  private String XErpToken;

  private String secret;

  private static HashMap<String, String> allAgencyMap = null;
  private static HashMap<String, String> allAgencyNameAndSourceMap = null;

  private SalesUserServiceImpl salesUserService;

  @Autowired
  public ErpConfig(SalesUserServiceImpl salesUserService) {
    this.salesUserService = salesUserService;
  }

  public HashMap<String, String> getAllAgencyMap() {
    if (allAgencyMap == null) {
      synchronized (ErpConfig.class) {
        HashMap<String, String> tempHashMap = salesUserService.getAllAgencyMap();
        if(tempHashMap == null) {
          throw new RequestException(ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getValue(), "获取经销商外部编码map为空，请检查经销商会员服务是否挂了");
        }
        if (tempHashMap.size() == 0) {
          throw new RequestException(ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getValue(), "获取经销商外部编码map 数量为0");
        }

        allAgencyMap = tempHashMap;
        return allAgencyMap;
      }
    }

    return allAgencyMap;
  }

  public synchronized void reloadAllAgencyMap() {
    HashMap<String, String> tempHashMap = salesUserService.getAllAgencyMap();
    if (tempHashMap == null || tempHashMap.size() == 0) {
      logger().error("获取经销商及门店名称map 数量为0");
      return ;
    }

    allAgencyMap = tempHashMap;
  }

  public HashMap<String, String> getAllAgencyAndShopNameAndSourceMap() {
    if (allAgencyNameAndSourceMap == null) {
      synchronized (ErpConfig.class) {
        HashMap<String, String> tempHashMap = salesUserService.getAllAgencyAndShopNameAndSourceMap();
        if (tempHashMap == null || tempHashMap.size() == 0) {
          logger().error("获取经销商及门店名称map 数量为0");
          return new HashMap<String, String>(0);
        }

        allAgencyNameAndSourceMap = tempHashMap;
        return allAgencyNameAndSourceMap;
      }
    }

    return allAgencyNameAndSourceMap;
  }

  public synchronized void reloadAllAgencyAndShopNameAndSourceMap() {
    HashMap<String, String> tempHashMap = salesUserService.getAllAgencyAndShopNameAndSourceMap();
    if (tempHashMap == null || tempHashMap.size() == 0) {
      logger().error("获取经销商及门店名称map 数量为0");
      return ;
    }
    allAgencyNameAndSourceMap = tempHashMap;
  }


}
