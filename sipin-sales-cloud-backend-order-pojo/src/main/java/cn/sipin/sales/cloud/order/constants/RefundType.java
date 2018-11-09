/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.constants;

public enum RefundType {
  CASH((byte) 0, "现金"),
  RETURN_ORIGINAL_WAY((byte) 1, "原路返回"),
  OTHER((byte) 2, "其他");

  private Byte value;

  private String description;

  RefundType(Byte value, String description) {
    this.value = value;
    this.description = description;
  }

  public Byte getValue() {
    return value;
  }

  public static Boolean contains(Byte value) {
    for (RefundType refundType : RefundType.values()) {

      if (refundType.value.equals(value)) {
        return true;
      }
    }
    return false;
  }

  public String getDescription() {
    return description;
  }

  public static String getDescriptionByValue(byte value){
    RefundType[] refundTypes = RefundType.values();
    String description = "";
    for (int i = 0,size = refundTypes.length; i < size; i++) {
      RefundType refundType = refundTypes[i];
      if(refundType.getValue() == value){
        description =  refundType.getDescription();
        break;
      }
    }
    return  description;
  }
}
