/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.constants;

/**
 * '优惠折扣类型: 优惠折扣类型
 *
 * 0：优惠券-满减 1：优惠券-满折 2: 积分折算金额 3: 整单优惠
 *
 * 优惠券-满减\优惠券-满折\积分折算金额: 发生在订单生成前
 *
 * 整单优惠： 发生在订单生成后
 */
public enum OrderDiscountTypeId {

  COUPON_MONEY((byte) 0, "优惠券-满减"),
  COUPON_DISCOUNT((byte) 1, "优惠券-满折"),
  PONIT_MONEY((byte) 2, "积分折算金额"),
  ALL_ORDERS((byte) 3, "整单优惠");

  private Byte value;

  private String description;

  OrderDiscountTypeId(Byte value, String description) {
    this.value = value;
    this.description = description;
  }

  public Byte getValue() {
    return value;
  }

  public Boolean checkCouponType(Byte typeId) {
    if(COUPON_MONEY.value.equals(typeId) || COUPON_DISCOUNT.value.equals(typeId)) {
      return true;
    }
    return false;
  }

}
