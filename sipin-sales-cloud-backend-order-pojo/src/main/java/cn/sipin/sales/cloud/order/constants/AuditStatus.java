/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.constants;

public enum AuditStatus {

  PENDING((byte)1, "待审核"),

  AUDITED((byte)2, "已审核"),
  CANCEL((byte)3, "取消");

  private Byte value;

  private String description;

  AuditStatus(Byte value, String description) {
    this.value = value;
    this.description = description;
  }

  public Byte getValue() {
    return value;
  }

  public String getDescription() {
    return description;
  }

  public static String getDescriptionByValue(byte value){
    AuditStatus[] auditStatuses = AuditStatus.values();
    String description = "";
    for (int i = 0,size = auditStatuses.length; i < size; i++) {
      AuditStatus status = auditStatuses[i];
      if(status.getValue() == value){
        description =  status.getDescription();
        break;
      }
    }
    return  description;
  }

}
