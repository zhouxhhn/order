/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.response;

import java.util.ArrayList;
import java.util.List;

import cn.sipin.sales.cloud.order.response.backend.detail.LogisticResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "OrderConsigneeResponse")
public class OrderConsigneeResponse {

  /**
   * 收货人
   */
  @ApiModelProperty(value = "收货人")
  private String name;

  /**
   * 收货人手机
   */
  @ApiModelProperty(value = "手机")
  private String mobile;

  /**
   * 省
   */
  @ApiModelProperty(value = "省")
  private String province;

  /**
   * 市
   */
  @ApiModelProperty(value = "市")
  private String city;

  /**
   * 区
   */
  @ApiModelProperty(value = "区")
  private String district;

  /**
   * 详细地址
   */
  @ApiModelProperty(value = "详细地址")
  private String addr;

  /**
   * 物流公司信息
   */
  @ApiModelProperty(value = "物流公司信息")
  private List<LogisticResponse> logisticResponseList = new ArrayList<>();
}
