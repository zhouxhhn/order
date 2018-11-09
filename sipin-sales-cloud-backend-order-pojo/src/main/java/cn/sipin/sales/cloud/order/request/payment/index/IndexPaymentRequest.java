/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.request.payment.index;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Api("支付流水请求参数封装类")
@Data
public class IndexPaymentRequest {

  @ApiModelProperty(value = "开始时间,时间格式为：yyyy-MM-dd HH:mm:ss")
  private String beforeAt;

  @ApiModelProperty(value = "结束时间,时间格式为：yyyy-MM-dd HH:mm:ss")
  private String endAt;

  @ApiModelProperty(value = "订单号")
  private String orderNo;
}
