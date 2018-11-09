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
 * 销售退货单明细
 * </p>
 *
 */
@Data
@Accessors(chain = true)
@TableName("return_order_detail")
public class ReturnOrderDetail extends Model<ReturnOrderDetail> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 退货单ID
     */
    @TableField("return_order_id")
    private Long returnOrderId;
    /**
     * 销售订单详情ID
     */
    @TableField("order_detail_id")
    private Long orderDetailId;
    /**
     * 退款详情号
     */
    @TableField("detail_no")
    private String detailNo;
    /**
     * SKU NO
     */
    @TableField("sku_no")
    private String skuNo;
    /**
     * 退货数量
     */
    @TableField("refunded_quantity")
    private Integer refundedQuantity;
    /**
     * 订单详情退款总金额
     */
    @TableField("refunded_amount")
    private BigDecimal refundedAmount;
    /**
     * 软删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;
    /**
     * 备注
     */
    private String remark;
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

    public ReturnOrderDetail() {
    }

    public ReturnOrderDetail(Long returnOrderId) {
        this.returnOrderId = returnOrderId;
    }

    @Override protected Serializable pkVal() {
        return this.id;
    }
}
