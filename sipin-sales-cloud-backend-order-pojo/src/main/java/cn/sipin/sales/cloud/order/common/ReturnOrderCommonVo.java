package cn.sipin.sales.cloud.order.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ReturnOrderCommonVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("退货单号")
    private String no;

    @ApiModelProperty("状态")
    private Integer statusId;

    @ApiModelProperty("退款方式")
    private Integer refundType;

    @ApiModelProperty("退款总金额")
    private BigDecimal refundedAmount;

    @ApiModelProperty("经销商Code")
    private String agencyCode;

    @ApiModelProperty("店铺Code")
    private String shopCode;

    @ApiModelProperty("用户手机号 匿名用户时填Anonymous-User")
    private String mobile;

    @ApiModelProperty("创建者ID")
    private Long creatorId;

    @ApiModelProperty("审核者ID")
    private Long auditorId;

    @ApiModelProperty("审核时间")
    private LocalDateTime auditedAt;

    @ApiModelProperty("退款原因")
    private String reasonNote;

    @ApiModelProperty("管理员备注")
    private String adminNote;

    @ApiModelProperty("软删除")
    private Integer isDeleted;

    @ApiModelProperty("创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty("更新时间")
    private LocalDateTime updatedAt;

    @ApiModelProperty("完成时间")
    private LocalDateTime finishedAt;

}
