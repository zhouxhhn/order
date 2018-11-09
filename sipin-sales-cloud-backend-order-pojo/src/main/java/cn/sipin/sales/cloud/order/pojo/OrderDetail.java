package cn.sipin.sales.cloud.order.pojo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单明细
 * </p>
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("order_detail")
public class OrderDetail extends Model<OrderDetail> {

  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 订单ID
   */
  @TableField("order_id")
  private Long orderId;

  /**
   * 订单商品交易号
   */
  @TableField("detail_no")
  private String detailNo;

  /**
   * SKU
   */
  @TableField("sku_sn")
  private String skuSn;

  /**
   * 产品SKU编号
   */
  @TableField("sku_no")
  private String skuNo;

  /**
   * 商品名称
   */
  private String name;

  /**
   * 物料规格
   */
  private String specification;

  /**
   * 物料材质
   */
  private String texture;

  /**
   * 物料颜色
   */
  private String color;

  /**
   * 图片地址
   */
  @TableField("img_path")
  private String imgPath;

  /**
   * 数量
   */
  private Integer quantity;

  /**
   * 原价
   */
  @TableField("original_amount")
  private BigDecimal originalAmount;

  /**
   * 售价
   */
  private BigDecimal amount;

  /**
   * 折后价
   */
  @TableField("discount_amount")
  private BigDecimal discountAmount;

  /**
   * 订单详情可退金额
   */
  @TableField("refund_amount")
  private BigDecimal refundAmount;

  /**
   * 订单详情可退数量
   */
  @TableField("refund_quantity")
  private Integer refundQuantity;


  /**
   * 实际应收金额
   */
  @TableField("real_amount")
  private BigDecimal realAmount;

  /**
   * 实际应收单价
   */
  @TableField("real_single_amount")
  private BigDecimal realSingleAmount;



  /**
   * 小计
   */
  private BigDecimal subtotal;

  /**
   * 是否自提
   */
  @TableField("is_pickup")
  private Boolean pickup;

  /**
   * 是否样品
   */
  @TableField("is_sample")
  private Boolean sample;

  /**
   * 是否赠品
   */
  @TableField("is_gift")
  private Boolean gift;

  /**
   * 备注
   */
  private String note;

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
   * 是否含有退货单
   */
  @TableField(exist = false)
  private boolean isReturn ;

  public OrderDetail() {
  }

  public OrderDetail(Long orderId) {
    this.orderId = orderId;
  }

  @Override protected Serializable pkVal() {
    return this.id;
  }
}
