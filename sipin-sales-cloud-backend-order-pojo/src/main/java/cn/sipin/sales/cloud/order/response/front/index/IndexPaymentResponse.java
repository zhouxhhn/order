/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.response.front.index;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class IndexPaymentResponse {


  @ApiModelProperty("交易流水号")
  private String paymentNo;

  @ApiModelProperty("交易时间")
  private LocalDateTime paidAt;

  @ApiModelProperty("交易类型")
  private String exchangeType;

  @ApiModelProperty("交易方式")
  private String exchangeMode;

  @ApiModelProperty("交易金额")
  private BigDecimal realReceivePrice;

  @ApiModelProperty("操作员")
  private String operator;

}
