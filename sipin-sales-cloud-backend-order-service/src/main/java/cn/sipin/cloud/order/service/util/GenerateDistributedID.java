/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.util;

import org.apache.commons.lang.RandomStringUtils;

public class GenerateDistributedID {

  private GenerateDistributedID() {
  }

  private static final String SEED = "seed";

  /**
   * 返回位数为8的分布式ID
   */
  private static String getNextId() {
    String nextId = String.valueOf(SnowFlake.getInstance().nextId());
    int length = nextId.length();
    return nextId.substring(length - 8, length);
  }

  public static String getSalesOrderNo(String agentCode) {

    int middle = Math.abs((agentCode + SEED).hashCode() % 99);
    String prefix = RandomStringUtils.randomNumeric(2);
    while (prefix.startsWith("0")) {
      prefix = RandomStringUtils.randomNumeric(2);
    }

    // 返回13位数字
    return "9" + prefix + String.valueOf(middle) + getNextId();
  }

  public static String getReturnOrderNo(String agentCode) {

    int middle = Math.abs((agentCode + SEED).hashCode() % 99);
    String prefix = RandomStringUtils.randomNumeric(2);
    while (prefix.startsWith("0")) {
      prefix = RandomStringUtils.randomNumeric(2);
    }

    // 返回13位数字
    return "1" + prefix + String.valueOf(middle) + getNextId();
  }

  /**
   * 退款支付流水号
   */
  public static String getReturnPaymentNo(String orderId) {

    int middle = Math.abs((orderId + SEED).hashCode() % 99);
    String prefix = RandomStringUtils.randomNumeric(2);
    while (prefix.startsWith("0")) {
      prefix = RandomStringUtils.randomNumeric(2);
    }

    // 返回13位数字
    return "7" + prefix + String.valueOf(middle) + getNextId();
  }

}
