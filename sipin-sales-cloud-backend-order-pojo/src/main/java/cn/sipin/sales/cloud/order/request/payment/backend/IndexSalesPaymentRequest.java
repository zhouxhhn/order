/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.request.payment.backend;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Api("销售资金流水请求参数封装类")
@Data
public class IndexSalesPaymentRequest {

  @ApiModelProperty(value = "开始时间,时间格式为：yyyy-MM-dd HH:mm:ss")
  private String beforeAt;

  @ApiModelProperty(value = "结束时间,时间格式为：yyyy-MM-dd HH:mm:ss")
  private String endAt;

  @ApiModelProperty(value = "销售订单号")
  private String orderNo;

  @ApiModelProperty(value = "门店code")
  private String shopCode;

  @ApiModelProperty(value = "经销商code")
  private String agencyCode;

  @ApiModelProperty("类型:1代表退款,0代表收款")
  private String exchangeType;

  @ApiModelProperty("交易方式")
  private String exchangeMode;
}
