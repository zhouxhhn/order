/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.constants;

public enum  OrderConstants {

  ORDER_MOBILE("Anonymous-User","匿名");

  private String value;

  private String description;

  OrderConstants(String value, String description) {
    this.value = value;
    this.description = description;
  }

  public String getValue() {
    return value;
  }

  public String getDescription() {
    return description;
  }
}
