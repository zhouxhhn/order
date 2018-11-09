package cn.sipin.sales.cloud.order.pojo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单收货人
 * </p>
 *
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("order_consignee")
public class OrderConsignee extends Model<OrderConsignee> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 订单ID
     */
    @TableField("order_id")
    private Long orderId;
    /**
     * 收货人
     */
    private String name;
    /**
     * 收货人手机
     */
    private String mobile;
    /**
     * 收货人电话
     */
    private String phone;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String district;
    /**
     * 详细地址
     */
    private String addr;
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

    @Override protected Serializable pkVal() {
        return this.id;
    }
}
