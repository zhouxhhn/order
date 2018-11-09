package cn.sipin.sales.cloud.order.pojo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 销售退货单
 * </p>
 */
@Data
@Accessors(chain = true)
@TableName("return_order")
public class ReturnOrder extends Model<ReturnOrder> {

  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 订单ID
   */
  @TableField("order_id")
  private Long orderId;

  /**
   * 退货单号
   */
  private String no;

  /**
   * 状态
   */
  @TableField("status_id")
  private Byte statusId;

  /**
   * 退款方式
   */
  @TableField("refund_type")
  private Byte refundType;

  /**
   * 退款总金额
   */
  @TableField("refunded_amount")
  private BigDecimal refundedAmount;

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
   * 创建者ID
   */
  @TableField("creator_id")
  private Long creatorId;

  /**
   * 审核者ID
   */
  @TableField("auditor_id")
  private Long auditorId;

  /**
   * 审核时间
   */
  @TableField("audited_at")
  private LocalDateTime auditedAt;

  /**
   * 退款原因
   */
  @TableField("reason_note")
  private String reasonNote;

  /**
   * 管理员备注
   */
  @TableField("admin_note")
  private String adminNote;


  /**
   * 是否已退积分
   */
  @TableField("is_return_point")
  private Boolean isReturnPoint;



  /**
   * 已退积分数
   */
  @TableField("return_point")
  private Integer returnPoint;

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
   * 完成时间
   */
  @TableField("finished_at")
  private LocalDateTime finishedAt;

  /**
   * 销售订单号
   */
  @TableField(exist = false)
  private String salesOrderNo;

  public ReturnOrder() {
  }

  public ReturnOrder(Long orderId) {
    this.orderId = orderId;
  }

  @Override protected Serializable pkVal() {
    return this.id;
  }
}
