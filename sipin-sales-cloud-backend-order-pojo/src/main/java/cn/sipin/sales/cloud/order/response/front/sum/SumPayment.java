/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.response.front.sum;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class SumPayment {


  @ApiModelProperty("类型")
  private String type;


  @ApiModelProperty("金额")
  private BigDecimal price;

  @ApiModelProperty("笔数")
  private Long total;

}
