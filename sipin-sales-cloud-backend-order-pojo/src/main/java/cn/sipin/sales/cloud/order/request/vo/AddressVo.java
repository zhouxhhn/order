/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.request.vo;

import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "收货信息(AddressVo)")
@Data
public class AddressVo {

  /**
   * 收货人
   */
  @NotNull(message = "收货人不能为空")
  @ApiModelProperty(value = "收货人", required = true)
  private String name;

  /**
   * 手机号码
   */
  @NotNull(message = "手机号码不能为空")
  private String mobile;

  /**
   * 详细地址
   */
  @NotNull(message = "详细地址不能为空")
  private String addr;

  /**
   * 省
   */
  @NotNull(message = "省不能为空")
  @ApiModelProperty(value = "省", required = true)
  private String province;

  /**
   * 市code
   */
  @NotNull(message = "市不能为空")
  @ApiModelProperty(value = "市", required = true)
  private String city;

  /**
   * 区code
   */
  @NotNull(message = "区code不能为空")
  @ApiModelProperty(value = "区", required = true)
  private String district;

  public Boolean isBlankAddress() {
    if (
        StringUtils.isBlank(name) &&
        StringUtils.isBlank(mobile) &&
        StringUtils.isBlank(addr) &&
        StringUtils.isBlank(province) &&
        StringUtils.isBlank(city) &&
        StringUtils.isBlank(district)) {
      return true;
    }

    return false;
  }
}
