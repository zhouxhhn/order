package cn.sipin.sales.cloud.order.response.returnOrder;

import cn.sipin.sales.cloud.order.common.ReturnOrderDetailCommonVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ReturnOrderDetailSearchVo extends ReturnOrderDetailCommonVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("订单商品交易号")
    private String orderDetailNo;

    /**
     * 是否赠品
     */
    @ApiModelProperty("是否赠品")
    private Integer isGift = 0;

    /**
     * 是否自提
     */
    @ApiModelProperty("是否自提")
    private Integer isPickup;

}
