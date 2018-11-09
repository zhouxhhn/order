/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.response.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "OrderConsigneeVo")
public class OrderConsigneeVo {
  @ApiModelProperty(value = "收货人名称")
  private String name;
  /**
   * 收货人手机
   */
  @ApiModelProperty(value = "收货人手机")
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
}
