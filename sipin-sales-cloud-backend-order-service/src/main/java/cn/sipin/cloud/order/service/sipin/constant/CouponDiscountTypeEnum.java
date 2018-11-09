package cn.sipin.cloud.order.service.sipin.constant;

import com.google.gson.annotations.SerializedName;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 优惠券优惠类型
 *
 * @author Sipin Backend Development Team
 */
public enum CouponDiscountTypeEnum {
  /** 满减 */
  @SerializedName("0")
  MONEY(0),

  /** 满折 */
  @SerializedName("1")
  DISCOUNT(1);

  private final Integer type;

  CouponDiscountTypeEnum(Integer type) {
    this.type = type;
  }

  @JsonValue
  public Integer type() {
    return this.type;
  }
}
