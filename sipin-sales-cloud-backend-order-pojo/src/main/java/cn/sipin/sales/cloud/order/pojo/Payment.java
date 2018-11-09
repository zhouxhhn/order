package cn.sipin.sales.cloud.order.pojo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 支付流水信息表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Payment extends Model<Payment> {

  private static final long serialVersionUID = 1L;

  /**
   * id
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 订单ID
   */
  @TableField("order_id")
  private Long orderId;

  /**
   * 收银员ID
   */
  @TableField("cashier_id")
  private Long cashierId;

  /**
   * 支付流水号
   */
  @TableField("payment_no")
  private String paymentNo;

  /**
   * 支付时间
   */
  @TableField("paid_at")
  private LocalDateTime paidAt;

  /**
   * 支付状态
   */
  @TableField("status_id")
  private Integer statusId;

  /**
   * 支付金额
   */
  private BigDecimal price;


  /**
   * 实收金额
   */
  @TableField("real_receive_price")
  private BigDecimal realReceivePrice;

  /**
   * 找零
   */
  @TableField("give_change")
  private BigDecimal giveChange;

  /**
   * 用户所使用用卡组织/第三方支付
   */
  @TableField("partner_id")
  private String partnerId;

  /**
   * 第三方交易号
   */
  @TableField("partner_trade_no")
  private String partnerTradeNo;

  /**
   * 商户号
   */
  @TableField("merchant_number")
  private String merchantNumber;

  /**
   * 商户名
   */
  @TableField("merchant_name")
  private String merchantName;

  /**
   * 用户交易账号
   */
  private String account;

  /**
   * 软删除
   */
  @TableField("is_deleted")
  @TableLogic
  private Integer isDeleted;

  /**
   * 创建时间
   */
  @TableField("created_at")
  private LocalDateTime createdAt;

  /**
   * 更新时间
   */
  @TableField("updated_at")
  private LocalDateTime updatedAt;

  /**
   * 门店code
   */
  @TableField(exist = false)
  private String shopCode;

  /**
   * 经销商code
   */
  @TableField(exist = false)
  private String agencyCode;

  /**
   * 订单号
   */
  @TableField(exist = false)
  private String no;


  public Payment(){

  }

  public Payment(Long orderId){
    this.orderId = orderId;
  }

  @Override protected Serializable pkVal() {

    return this.id;
  }
}
