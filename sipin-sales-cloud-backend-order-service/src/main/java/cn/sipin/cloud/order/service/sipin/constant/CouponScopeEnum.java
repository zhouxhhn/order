package cn.sipin.cloud.order.service.sipin.constant;

import com.google.gson.annotations.SerializedName;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 优惠券适用范围
 *
 * @author Sipin Backend Development Team
 */
public enum CouponScopeEnum {
  /** 全场 */
  @SerializedName("0")
  OVERALL(0),

  /** 指定商品 */
  @SerializedName("2")
  GOODS(2);

  private final Integer value;

  CouponScopeEnum(Integer value) {
    this.value = value;
  }

  @JsonValue
  public Integer value() {
    return this.value;
  }
}
