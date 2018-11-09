/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.response.backend.index;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class IndexSalesPaymentResponse {

  @ApiModelProperty("订单号")
  private String no;

  @ApiModelProperty("交易流水号")
  private String paymentNo;

  @ApiModelProperty("交易时间")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime paidAt;

  @ApiModelProperty("经销商code")
  private String agencyCode;

  @ApiModelProperty("经销商名字")
  private String agencyName;

  @ApiModelProperty("门店code")
  private String shopCode;

  @ApiModelProperty("门店名字")
  private String shopName;

  @ApiModelProperty("交易类型")
  private String exchangeType;

  @ApiModelProperty("交易方式")
  private String exchangeMode;

  @ApiModelProperty("交易金额")
  private BigDecimal price;

  @ApiModelProperty("找零")
  private BigDecimal giveChange;

  @ApiModelProperty("实际发生金额")
  private BigDecimal realReceivePrice;

  @ApiModelProperty("操作员")
  private String operator;

}
