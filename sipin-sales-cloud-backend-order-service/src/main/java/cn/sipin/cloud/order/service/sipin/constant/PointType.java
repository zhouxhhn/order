/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.sipin.constant;

/**
 * 积分调整类型：5为调减、24为调增
 */
public enum PointType {
  INCREASE((byte)24, "调增"),
  DECREASE((byte)5, "调减");

  private Byte value;
  private String description;

  PointType(Byte value, String description) {
    this.value = value;
    this.description = description;
  }

  public Byte getValue() {
    return value;
  }
}
