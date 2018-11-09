/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.response;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SalesOrderDetailResponse")
public class SalesOrderDetailResponse {

  /**
   * 订单商品交易号
   */
  @ApiModelProperty(value = "订单商品交易号")
  private String detailNo;

  /**
   * SKU
   */
  @ApiModelProperty(value = "SKU")
  private String skuSn;

  /**
   * 产品SKU编号
   */
  @ApiModelProperty(value = "产品SKU编号")
  private String skuNo;


  /**
   * 退货数量
   */
  @ApiModelProperty(value = "退货数量")
  private Integer refundedQuantity;
  /**
   * 订单详情退款总金额
   */
  @ApiModelProperty(value = "订单详情退款总金额")
  private BigDecimal refundedAmount;


  /**
   * 商品名称
   */
  @ApiModelProperty(value = "商品名称")
  private String name;

  /**
   * 物料规格
   */
  @ApiModelProperty(value = "物料规格")
  private String specification;

  /**
   * 物料材质
   */
  @ApiModelProperty(value = "物料材质")
  private String texture;

  /**
   * 物料颜色
   */
  @ApiModelProperty(value = "物料颜色")
  private String color;

  /**
   * 图片地址
   */
  @ApiModelProperty(value = "图片地址")
  private String imgPath;

  /**
   * 数量
   */
  @ApiModelProperty(value = "数量")
  private Integer quantity;

  /**
   * 售价
   */
  @ApiModelProperty(value = "售价")
  private BigDecimal amount;

  /**
   * 折后价
   */
  @ApiModelProperty(value = "折后价")
  private BigDecimal discountAmount;

  /**
   * 实际应收金额
   */
  @ApiModelProperty(value = "实际应收金额")
  private BigDecimal realAmount;

  /**
   * 实际应收单价
   */
  @ApiModelProperty(value = "实际应收单价")
  private BigDecimal realSingleAmount;


  /**
   * 小计
   */
  @ApiModelProperty(value = "小计")
  private BigDecimal subtotal;

  /**
   * 是否自提
   */
  @ApiModelProperty(value = "是否自提")
  private Boolean isPickup;

  /**
   * 是否样品
   */
  @ApiModelProperty(value = "是否样品")
  private Boolean isSample;

  /**
   * 是否赠品
   */
  @ApiModelProperty(value = "是否赠品")
  private Boolean isGift;

  /**
   * 备注
   */
  @ApiModelProperty(value = "备注")
  private String note;

  /**
   * 属性
   */
  @ApiModelProperty(value = "属性")
  private String property;

  /**
   * 组合total价格
   */
  @ApiModelProperty(value = "组合total价格")
  private BigDecimal tzTotal;

  /**
   * 组合realAmount价格
   */
  @ApiModelProperty(value = "组合realAmount价格")
  private BigDecimal tzRealAmount;
}
