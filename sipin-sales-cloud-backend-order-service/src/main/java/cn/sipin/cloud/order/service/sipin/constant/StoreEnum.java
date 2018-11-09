package cn.sipin.cloud.order.service.sipin.constant;

import com.google.gson.annotations.SerializedName;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 店铺枚举
 *
 * @author Sipin Backend Development Team
 */
public enum StoreEnum {
  /**
   * 斯品广州店
   */
  @SerializedName("6")
  GUANGZHOU(6),
  /**
   * 斯品上海店
   */
  @SerializedName("7")
  SHANGHAI(7),
  /**
   * 斯品珠海店
   */
  @SerializedName("8")
  ZHUHAI(8);

  private Integer storeId;

  StoreEnum(Integer storeId) {
    this.storeId = storeId;
  }

  @JsonValue
  public Integer value() {
    return storeId;
  }
}
