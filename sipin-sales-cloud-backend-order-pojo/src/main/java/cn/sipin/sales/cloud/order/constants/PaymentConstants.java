/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.constants;

/**
 * 收银支付方式
 */
public enum PaymentConstants {
  //收款支付方式
  WEIXIN_PAYMENT("WXP","微信"),
  ALI_PAYMENT("ALP","支付宝"),
  CASHPAYMENT("cashPayment","现金支付"),
  POSPAYMENT("cardPayment","刷卡"),
  SCANPAYMENT("scanPayment","扫码支付"),
  OTHERSPAYMENT("othersPayment","其它"),
  //退款方式：现金
  RETURN_CASH_PAYMENT("0","现金"),
  //退款方式：其它
  RETURN_OTHERS_PAYMENT("2","其它"),
  RECEIVE_PAYMENT("receivePayment","收款"),
  REBACK_PAYMENT("rebackPayment","退款"),
  //后台资金流水类型
  EXCHANGE_TYPE("1","203"),
  COUNT_PAYMENT("countPayment","累计");

  
  private String value;

  private String description;

  PaymentConstants(String value, String description) {
    this.value = value;
    this.description = description;
  }

  public String getValue() {
    return value;
  }

  public String getDescription() {
    return description;
  }

  public static String getDescriptionByValue(String value){
    if (value == null) {
      return null;
    }
    for (PaymentConstants paymentConstants : PaymentConstants.values()) {
      if (paymentConstants.value.equals(value)) {
        return paymentConstants.getDescription();
      }
    }
    return null;
  }
}
