/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "MemberAddressRequest")
public class MemberAddressRequest {

  @ApiModelProperty("省ID")
  private Integer provinceId;

  @ApiModelProperty("省名称")
  private String provinceName;

  @ApiModelProperty("城市ID")
  private Integer cityId;

  @ApiModelProperty("城市名")
  private String cityName;

  @ApiModelProperty("区域ID")
  private Integer districtId;

  @ApiModelProperty("区域名")
  private String districtName;

  @ApiModelProperty("地址")
  private String address;

  @ApiModelProperty("地址收件人名称")
  private String consignee;

  @ApiModelProperty("收件人手机号")
  private String mobile;

  @ApiModelProperty("是否是默认地址")
  private Byte isDefault;
}
