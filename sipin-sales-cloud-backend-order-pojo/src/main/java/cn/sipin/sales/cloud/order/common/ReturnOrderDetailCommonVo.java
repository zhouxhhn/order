package cn.sipin.sales.cloud.order.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ReturnOrderDetailCommonVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("退货单ID")
    private Long returnOrderId;

    @ApiModelProperty("销售订单详情ID")
    private Long orderDetailId;

    @ApiModelProperty("退款详情号")
    private String detailNo;

    @ApiModelProperty("SKU NO")
    private String skuNo;

    @ApiModelProperty("退货数量")
    private Integer refundedQuantity;

    @ApiModelProperty("订单详情退款总金额")
    private BigDecimal refundedAmount;

    @ApiModelProperty("软删除")
    private Integer isDeleted;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedAt;

}
