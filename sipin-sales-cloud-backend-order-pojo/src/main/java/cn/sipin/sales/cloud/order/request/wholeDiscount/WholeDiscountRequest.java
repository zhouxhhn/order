/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.request.wholeDiscount;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WholeDiscountRequest {

  @NotNull
  @ApiModelProperty(value = "应收金额",required = true)
  private BigDecimal payableAmount;
}
