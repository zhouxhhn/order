/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.response;

import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SalesOrderDiscountResponse")
public class SalesOrderDiscountResponse {

  /**
   * 优惠折扣类型，0：优惠券-满减 1：优惠券-满折 2: 积分折算金额 3: 整单优惠 4：抹零
   */
  @ApiModelProperty(value = "优惠折扣类型")
  private Byte typeId;
  /**
   * 优惠券作用的订单号
   */
  @ApiModelProperty(value = "优惠券作用的订单号")
  private Long orderId;
  /**
   * 优惠券
   */
  @ApiModelProperty(value = "优惠券")
  private String code;

  /**
   * 优惠券面值/折扣
   */
  @ApiModelProperty(value = "优惠券面值/折扣")
  private BigDecimal discountValue;

}
