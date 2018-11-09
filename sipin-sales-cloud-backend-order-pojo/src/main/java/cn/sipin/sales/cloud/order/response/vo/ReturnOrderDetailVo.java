/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.response.vo;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ReturnOrderDetailVo")
public class ReturnOrderDetailVo {

  /**
   * 销售订单详情交易号
   */
  @ApiModelProperty(value = "销售订单详情交易号")
  private String salesOrderDetailNo;


  /**
   * 退款详情号
   */
  @ApiModelProperty(value = "退款详情号")
  private String detailNo;
  /**
   * SKU NO
   */
  @ApiModelProperty(value = "SKU NO")
  private String skuNo;

  /**
   * 名称
   */
  @ApiModelProperty(value = "name")
  private String name;

  /**
   * 实际销售价格
   */
  @ApiModelProperty(value = "实际销售价格")
  private BigDecimal salesPrice;

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
   * 软删除
   */
  @ApiModelProperty(value = "软删除")
  private Integer isDeleted;
  /**
   * 备注
   */
  @ApiModelProperty(value = "备注")
  private String remark;
  /**
   * 创建时间
   */
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @ApiModelProperty(value = "创建时间")
  private LocalDateTime createdAt;
  /**
   * 更新时间
   */
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @ApiModelProperty(value = "更新时间")
  private LocalDateTime updatedAt;
  
  
}
