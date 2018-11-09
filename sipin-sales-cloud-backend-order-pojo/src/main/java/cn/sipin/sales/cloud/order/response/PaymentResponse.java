/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.sales.cloud.order.response;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "PaymentResponse")
public class PaymentResponse {
  /**
   * 交易时间
   */
  @ApiModelProperty(value = "交易时间")
  private LocalDateTime paidAt;

  /**
   * 支付金额
   */
  @ApiModelProperty(value = " 支付金额")
  private BigDecimal price;

  /**
   * 用户所使用用卡组织/第三方支付
   */
  @ApiModelProperty(value = "支付方式")
  private String paymentMethod;

  /**
   * 第三方交易号
   */
  @ApiModelProperty(value = "交易号")
  private String partnerTradeNo;


  /**
   * 用户交易账号
   */
  @ApiModelProperty(value = "支付帐号")
  private String account;


}
