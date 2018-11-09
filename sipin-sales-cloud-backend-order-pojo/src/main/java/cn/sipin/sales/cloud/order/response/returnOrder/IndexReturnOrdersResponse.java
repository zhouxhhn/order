/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.response.returnOrder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import cn.sipin.sales.cloud.order.pojo.Payment;
import cn.sipin.sales.cloud.order.response.PaymentResponse;
import cn.sipin.sales.cloud.order.response.vo.ReturnOrderDetailVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "IndexReturnOrdersResponse")
public class IndexReturnOrdersResponse {

  /**
   * 销售单号
   */
  @ApiModelProperty(value = "销售单号")
  private String salesOrderNo;

  /**
   * 退货单号
   */
  @ApiModelProperty(value = "退货单号")
  private String no;

  /**
   * 状态
   */
  @ApiModelProperty(value = "状态")
  private Byte statusId;

  /**
   * 退款方式
   */
  @ApiModelProperty(value = "退款方式")
  private Byte refundType;

  /**
   * 退款总金额
   */
  @ApiModelProperty(value = "退款总金额")
  private BigDecimal refundedAmount;

  /**
   * 经销商Code
   */
  @ApiModelProperty(value = "经销商Code")
  private String agencyCode;

  /**
   * 经销商
   */
  @ApiModelProperty(value = "经销商")
  private String agencyName;


  /**
   * 店铺Code
   */
  @ApiModelProperty(value = "店铺Code")
  private String shopCode;

  /**
   * 店铺
   */
  @ApiModelProperty(value = "店铺")
  private String shopName;

  /**
   * 用户手机号 匿名用户时填Anonymous-User
   */
  @ApiModelProperty(value = "用户手机号")
  private String mobile;

  /**
   * 创建者ID
   */
  @ApiModelProperty(value = "创建者ID")
  private String creatorName;

  /**
   * 审核者ID
   */
  @ApiModelProperty(value = "审核者ID")
  private String auditorName;

  /**
   * 审核时间
   */
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @ApiModelProperty(value = "审核时间")
  private LocalDateTime auditedAt;

  /**
   * 退款原因
   */
  @ApiModelProperty(value = "退款原因")
  private String reasonNote;

  /**
   * 管理员备注
   */
  @ApiModelProperty(value = "管理员备注")
  private String adminNote;

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

  /**
   * 完成时间
   */
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @ApiModelProperty(value = "完成时间")
  private LocalDateTime finishedAt;

  /**
   * 退货单详情
   */
  @ApiModelProperty(value = "退货单详情")
  List<ReturnOrderDetailVo> returnOrderDetailVos;

  @ApiModelProperty(value = "支付列表")
  List<PaymentResponse> paymentList;

}
