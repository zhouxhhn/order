/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import cn.sipin.sales.cloud.order.request.vo.AddressVo;
import cn.sipin.sales.cloud.order.request.vo.OrderSkuDetailVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SalesOrderRequest")
public class SalesOrderRequest {

  @ApiModelProperty(value = "用户手机，匿名用户不用传")
  private String mobile;

  @ApiModelProperty(value = "优惠券code")
  private String couponCode;

  @ApiModelProperty(value = "积分")
  private Integer point;

  @NotNull(message = "订单商品详情")
  @ApiModelProperty(value = "订单商品详情")
  private List<OrderSkuDetailVo> orderSkuDetails;

  @ApiModelProperty(value = "收货地址 全部自提不用传")
  private AddressVo address;

  @NotNull(message = "导购员不能为空")
  @ApiModelProperty(value = "导购员")
  private Long sallerId;


  @ApiModelProperty(value = "备注")
  private String note;


  @NotNull(message = "sessionKey不能为空")
  private String sessionKey;

}
