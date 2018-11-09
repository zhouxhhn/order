/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.request.vo;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("退货单商品详情(ReturnSkuDetailVo)")
public class ReturnSkuDetailVo {

  @ApiModelProperty(value = "orderDetailNo订单详情交易号", required = true)
  private String orderDetailNo;

//  @ApiModelProperty(value = "sku编码", required = true)
//  @NotNull(message = "sku编码不能为空")
//  private String skuNo;

  @ApiModelProperty(value = "订单商品退货数", required = false)
  @NotNull(message = "订单商品退货数不能为空")
  private Integer quantity;


  @ApiModelProperty(value = "订单商品退货金额",required = false)
  @NotNull(message = "订单商品退货金额为空")
  private BigDecimal refundedAmount;


}
