/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SalesOrderResponse")
public class SalesOrderResponse {

  /**
   * 销售订单号
   */

  @ApiModelProperty(value = "销售订单号")
  private String no;

  /**
   * 状态ID
   */
  @ApiModelProperty(value = "状态")
  private Integer statusId;

  /**
   * 订单金额
   */
  @ApiModelProperty(value = "订单金额")
  private BigDecimal amount;

  /**
   * 原应收金额
   */
  @ApiModelProperty(value = "原应收金额")
  private BigDecimal originalPayableAmount;

  /**
   * 实际应收金额
   */
  @ApiModelProperty(value = "实际应收金额")
  private BigDecimal payableAmount;

  /**
   * 已收金额
   */
  @ApiModelProperty(value = "已收金额")
  private BigDecimal paidAmount;

  /**
   * 订单可退金额
   */
  @ApiModelProperty(value = "可退金额")
  private BigDecimal refundAmount;

  /**
   * 用户手机号 匿名用户时填Anonymous-User
   */
  @ApiModelProperty(value = "用户手机号")
  private String mobile;

  /**
   * 当前会员折扣
   */
  @ApiModelProperty(value = "当前会员折扣")
  private BigDecimal discount;

  /**
   * 已使用积分数
   */
  @ApiModelProperty(value = "已使用积分数")
  private Integer usedPoint;

  /**
   * 门店销售员
   */
  @ApiModelProperty(value = "门店销售员")
  private Long salerName;

  /**
   * 客户/销售员备注
   */
  @ApiModelProperty(value = "客户/销售员备注")
  private String note;

  /**
   * 管理员备注
   */
  @ApiModelProperty(value = "管理员备注")
  private String adminNote;

  /**
   * 取消时间
   */
  @ApiModelProperty(value = "取消时间")
  private LocalDateTime canceledAt;

  /**
   * 付款时间
   */
  @ApiModelProperty(value = "付款时间")
  private LocalDateTime paidAt;

  /**
   * 完成时间
   */
  @ApiModelProperty(value = "完成时间")
  private LocalDateTime finishedAt;

  /**
   * 是否为自提单
   */
  @ApiModelProperty(value = "是否为自提单")
  private Integer isPickup;


  @ApiModelProperty(value = "订单商品详情")
  List<SalesOrderDetailResponse> orderDetails;


}
