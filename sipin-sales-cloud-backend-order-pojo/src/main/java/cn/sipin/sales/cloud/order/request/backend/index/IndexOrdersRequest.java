/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.request.backend.index;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IndexOrdersRequest {

  @ApiModelProperty(value = "订单号")
  private String no;

  @ApiModelProperty(value = "下单时间")
  private String createdAt;

  @ApiModelProperty(value = "门店code")
  private String shopCode;

  @ApiModelProperty(value = "订单状态：全部不需要传入值，0为待付款，2为已付款，5为已完成")
  private Long statusId ;
}
