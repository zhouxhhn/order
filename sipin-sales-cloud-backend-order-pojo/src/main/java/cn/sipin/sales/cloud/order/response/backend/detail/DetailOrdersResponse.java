/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.response.backend.detail;

import com.baomidou.mybatisplus.annotations.TableField;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import cn.sipin.sales.cloud.order.pojo.OrderConsignee;
import cn.sipin.sales.cloud.order.pojo.Payment;
import cn.sipin.sales.cloud.order.response.OrderConsigneeResponse;
import cn.sipin.sales.cloud.order.response.PaymentResponse;
import cn.sipin.sales.cloud.order.response.SalesOrderDetailResponse;
import cn.sipin.sales.cloud.order.response.SalesOrderResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "DetailOrdersResponse")
public class DetailOrdersResponse{

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
   * 状态名
   */
  @ApiModelProperty(value = "状态名")
  private String status;


  /**
   * 订单金额
   */
  @ApiModelProperty(value = "商品总额")
  private BigDecimal amount;

  /**
   * 原应收金额
   */
  @ApiModelProperty(value = "原应收金额")
  private BigDecimal originalPayableAmount;

  /**
   * 实际应收金额
   */
  @ApiModelProperty(value = "应收金额")
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
  @ApiModelProperty(value = "门店导购员")
  private String salerName;

  /**
   * 操作员
   */
  @ApiModelProperty(value = "操作员")
  private String createrName;

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
  List<SalesOrderDetailResponse> orderDetailList;

  /**
   * 订单收货地址信息
   */
  @ApiModelProperty(value = "订单收货地址信息")
  private OrderConsigneeResponse orderConsignee;




  /**
   * 订单支付信息
   */
  @ApiModelProperty(value = "订单支付信息")
  private List<PaymentResponse> paymentResponseList = new ArrayList<>();

  /**
   * 门店名称
   */
  @ApiModelProperty(value = "门店名称")
  private String shopName ;

  /**
   * 经销商名称
   */
  @ApiModelProperty(value = "经销商名称")
  private String agencyName ;

  /**
   * 会员折扣优惠
   */
  @ApiModelProperty(value = "会员折扣优惠")
  private BigDecimal memberDiscount ;

  /**
   * 优惠券金额
   */
  @ApiModelProperty(value = "优惠券金额")
  private BigDecimal couponAmount = new BigDecimal(0);

  /**
   * 整单优惠金额
   */
  @ApiModelProperty(value = "整单优惠金额")
  private BigDecimal wholeDiscountAmount = new BigDecimal(0);

  /**
   * 积分抵扣
   */
  @ApiModelProperty(value = "积分抵扣")
  private BigDecimal pointsDiscount = new BigDecimal(0);

  /**
   * 现金
   */
  @ApiModelProperty(value = "现金")
  BigDecimal cashAmount = new BigDecimal(0);

  /**
   * 在线支付
   */
  @ApiModelProperty(value = "在线支付")
  BigDecimal onlinePayment = new BigDecimal(0);

  /**
   * 找零
   */
  @ApiModelProperty(value = "找零")
  BigDecimal giveChange = new BigDecimal(0);

  /**
   * 下单时间
   */
  @ApiModelProperty(value = "下单时间")
  private LocalDateTime createdAt;

  /**
   * 是否含有退货单
   */
  @ApiModelProperty(value = "是否含有退货单")
  private boolean isReturn ;

}
