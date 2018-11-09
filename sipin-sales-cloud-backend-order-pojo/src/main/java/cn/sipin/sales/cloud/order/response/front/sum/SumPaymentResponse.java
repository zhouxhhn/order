/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.response.front.sum;

import java.math.BigDecimal;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class SumPaymentResponse {

  @ApiModelProperty("收款列表")
  List<SumPayment> receivePaymentList;

  @ApiModelProperty("退款列表")
  List<SumPayment> backPaymentList;

  @ApiModelProperty("共计笔数")
  private Long totalCount;

  @ApiModelProperty("共计金额")
  private BigDecimal totalPrice;

}
