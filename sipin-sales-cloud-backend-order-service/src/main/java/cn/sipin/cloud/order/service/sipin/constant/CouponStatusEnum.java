package cn.sipin.cloud.order.service.sipin.constant;

import com.google.gson.annotations.SerializedName;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 优惠券状态枚举
 *
 * @author Sipin Backend Development Team
 */
public enum CouponStatusEnum {

  /** 可用 */
  @SerializedName("0")
  AVAILABLE(0),

  /** 不可用 */
  @SerializedName("1")
  DISABLED(1),

  /** 已过期 */
  @SerializedName("2")
  OUTDATED(2);

  private final Integer status;

  CouponStatusEnum(Integer status) {
    this.status = status;
  }

  @JsonValue
  public Integer status() {
    return this.status;
  }
}
