/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.request.vo;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("订单商品详情(OrderSkuDetailVo)")
public class OrderSkuDetailVo {
  @ApiModelProperty(value = "sku-sn", required = false)
  private String skuSn;

  @ApiModelProperty(value = "sku编码", required = true)
  @NotNull(message = "sku编码不能为空")
  private String skuNo;

  @ApiModelProperty(value = "sku售价",required = true)
  @NotNull(message = "sku售价不能为空")
  private String amount;

  @ApiModelProperty(value = "采购数", required = true)
  @NotNull(message = "采购数不能为空")
  private Integer quantity;

  @ApiModelProperty(value = "是否自提", required = true)
  @NotNull(message = "是否自提必填")
  private Boolean isPickup;

  @ApiModelProperty(value = "是否赠品", required = true)
  @NotNull(message = "是否赠品必填")
  private Boolean isGift;

  @ApiModelProperty(value = "是否样品", required = false)
  private Boolean isSample = false;

}
