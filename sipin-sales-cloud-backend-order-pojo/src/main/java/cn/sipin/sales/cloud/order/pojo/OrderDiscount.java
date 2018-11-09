package cn.sipin.sales.cloud.order.pojo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单优惠折扣
 * </p>
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_discount")
public class OrderDiscount extends Model<OrderDiscount> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 优惠折扣类型，0：优惠券-满减 1：优惠券-满折 2: 积分折算金额 3: 整单优惠 4：抹零
     */
    @TableField("type_id")
    private Byte typeId;
    /**
     * 优惠券作用的订单号
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 优惠券
     */
    private String code;

    /**
     * 优惠券面值/折扣
     */
    @TableField("discount_value")
    private BigDecimal discountValue;

    @Override protected Serializable pkVal() {
        return this.id;
    }
}
