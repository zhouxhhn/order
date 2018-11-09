package cn.sipin.sales.cloud.order.pojo;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单快递物流表
 * </p>
 *
 */
@Data
@Accessors(chain = true)
@TableName("order_express")
public class OrderExpress extends Model<OrderExpress> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 订单号
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 运单号
     */
    @TableField("express_no")
    private String expressNo;
    /**
     * K3物流公司名称
     */
    @TableField("express_company")
    private String expressCompany;
    /**
     * K3物流公司code
     */
    @TableField("express_company_code")
    private String expressCompanyCode;
    /**
     * 备注
     */
    private String note;
    /**
     * 创建时间
     */
    @TableField("created_at")
    private Date createdAt;
    /**
     * 更新时间
     */
    @TableField("updated_at")
    private Date updatedAt;

    @Override protected Serializable pkVal() {
        return this.id;
    }
}
