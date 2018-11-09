package cn.sipin.sales.cloud.order.pojo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 销售订单
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Orders extends Model<Orders> {


  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 销售订单号
   */
  private String no;

  /**
   * 状态ID
   */
  @TableField("status_id")
  private Integer statusId;

  /**
   * 订单金额
   */
  private BigDecimal amount;

  /**
   * 原应收金额 订单生成前的应收金额
   */
  @TableField("original_payable_amount")
  private BigDecimal originalPayableAmount;

  /**
   * 实际应收金额 订单生成后 可能会整单优惠后产生的应收金额
   */
  @TableField("payable_amount")
  private BigDecimal payableAmount;

  /**
   * 已收金额
   */
  @TableField("paid_amount")
  private BigDecimal paidAmount;



  /**
   * 订单可退金额
   */
  @TableField("refund_amount")
  private BigDecimal refundAmount;


  /**
   * 经销商Code
   */
  @TableField("agency_code")
  private String agencyCode;

  /**
   * 店铺Code
   */
  @TableField("shop_code")
  private String shopCode;

  /**
   * 用户手机号 匿名用户时填Anonymous-User
   */
  private String mobile;

  /**
   * 当前会员折扣
   */
  private BigDecimal discount;

  /**
   * 已使用积分数
   */
  @TableField("used_point")
  private Integer usedPoint;

  /**
   * 已使用积分数
   */
  @TableField("give_point")
  private Integer givePoint;

  /**
   * 已使用积分数
   */
  @TableField("return_point")
  private Integer returnPoint;


  /**
   * 门店销售员ID
   */
  @TableField("saler_id")
  private Long salerId;

  /**
   * 操作员ID
   */
  @TableField("creater_id")
  private Long createrId;

  /**
   * 客户/销售员备注
   */
  private String note;

  /**
   * 管理员备注
   */
  @TableField("admin_note")
  private String adminNote;

  /**
   * 取消时间
   */
  @TableField("canceled_at")
  private LocalDateTime canceledAt;

  /**
   * 付款时间
   */
  @TableField("paid_at")
  private LocalDateTime paidAt;

  /**
   * 完成时间
   */
  @TableField("finished_at")
  private LocalDateTime finishedAt;

  /**
   * 是否为自提单
   */
  @TableField("is_pickup")
  private Boolean pickup;

  /**
   * 软删除
   */
  @TableField("is_deleted")
  @TableLogic
  private Integer deleted;

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
   * 订单收货地址信息
   */
  @TableField(exist = false)
  private OrderConsignee orderConsignee;

  /**
   * 订单收货地址信息
   */
  @TableField(exist = false)
  private List<OrderDetail> orderDetailList;


  @Override protected Serializable pkVal() {
    return this.id;
  }



    /**
     * 订单支付信息
     */
    @TableField(exist = false)
    private List<Payment> paymentList;

    /**
     * 导购员
     */
    @TableField(exist = false)
    private String salerName ;

    /**
     * 操作员
     */
    @TableField(exist = false)
    private String createrName ;

    /**
     * 门店名称
     */
    @TableField(exist = false)
    private String shopName ;

    /**
     * 经销商名称
     */
    @TableField(exist = false)
    private String agencyName ;

  /**
   * 是否含有退货单
   */
  @TableField(exist = false)
  private boolean isReturn ;


  /**
   * 订单优惠折扣信息
   */
  @TableField(exist = false)
  private List<OrderDiscount> orderDiscountList;
}
