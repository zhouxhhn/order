/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.request.confirmPayment;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ConfirmPaymentRequest {

  @NotNull()
  @Min(value = 0)
  @ApiModelProperty(value = "支付金额",required = true)
  private BigDecimal payableAmount;

  @ApiModelProperty(value = "找零")
  private BigDecimal giveChange;

  @NotNull
  @ApiModelProperty(value = "支付类型：现金:cashPayment,刷卡:cardPayment,扫码支付:scanPayment",required = true)
  private String paymentType;

  @ApiModelProperty(value = "商户名")
  private String merchName;

  @ApiModelProperty(value = "扫码类型")
  private String cups;

  @ApiModelProperty(value = "商户号")
  private String merchId;


  @ApiModelProperty(value = "第三方交易号,流水号")
  private String traceNo;

  @ApiModelProperty(value = "用户所使用用卡组织/第三方支付")
  private String businessId;

  @ApiModelProperty(value = "用户交易账号")
  private String cardno;

  @ApiModelProperty(value = "收银员id")
  private Long createrId;

  @ApiModelProperty(value = "支付交易码")
  private String paymentTransactionCode;


}
