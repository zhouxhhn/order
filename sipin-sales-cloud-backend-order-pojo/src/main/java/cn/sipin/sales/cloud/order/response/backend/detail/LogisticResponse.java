/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.response.backend.detail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "DetailOrdersResponse")
public class LogisticResponse {

  /**
   * 物流公司
   */
  @ApiModelProperty(value = "物流公司")
  private String logisticsCompany;

  /**
   * 运单号
   */
  @ApiModelProperty(value = "运单号")
  private String waybillNumber;
}
