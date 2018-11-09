/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.request.returnOrder;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IndexReturnOrdersRequest {

  @ApiModelProperty(value = "退货单号")
  private String returnOrderNo;

  @ApiModelProperty(value = "订单号")
  private String orderNo;

  @ApiModelProperty(value = "下单开始日期,格式：yyyy-MM-dd")
  private String beginDate;

  @ApiModelProperty(value = "下单结束日期,格式：yyyy-MM-dd")
  private String endDate;

  @ApiModelProperty(value = "门店code")
  private String shopCode;


}
