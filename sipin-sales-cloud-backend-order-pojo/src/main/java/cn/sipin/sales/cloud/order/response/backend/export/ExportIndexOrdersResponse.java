/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.response.backend.export;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import cn.sipin.sales.cloud.order.response.SalesOrderDetailResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ExportIndexOrdersResponse")
public class ExportIndexOrdersResponse {

  /**
   * 订单号
   */
  @ApiModelProperty(value = "订单号")
  private String no;

  /**
   * 会员
   */
  @ApiModelProperty(value = "会员")
  private String mobile;

  /**
   * 门店
   */
  @ApiModelProperty(value = "门店")
  private String shopName;

  /**
   * 经销商
   */
  @ApiModelProperty(value = "经销商")
  private String agencyName;


  /**
   * 订单状态
   */
  @ApiModelProperty(value = "订单状态")
  private String status;

  /**
   * 导购员
   */
  @ApiModelProperty(value = "导购员")
  private String salerName;

  /**
   * 操作员
   */
  @ApiModelProperty(value = "操作员")
  private String createrName;

  /**
   * 备注
   */
  @ApiModelProperty(value = "备注")
  private String note;

  /**
   * 管理员备注
   */
  @ApiModelProperty(value = "管理员备注")
  private String adminNote;

  /**
   * 应付金额
   */
  @ApiModelProperty(value = "应付金额")
  private BigDecimal payableAmount;

  /**
   * 已付金额
   */
  @ApiModelProperty(value = "已付金额")
  private BigDecimal paidAmount;

  /**
   * 订单可退金额
   */
  @ApiModelProperty(value = "可退金额")
  private BigDecimal refundAmount;


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
   * 是否含有退货单
   */
  @ApiModelProperty(value = "退货单记录")
  private boolean isReturn ;

  private List<SalesOrderDetailResponse> orderDetailList;
}
