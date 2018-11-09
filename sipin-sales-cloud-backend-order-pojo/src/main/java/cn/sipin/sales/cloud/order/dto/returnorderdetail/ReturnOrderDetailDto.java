package cn.sipin.sales.cloud.order.dto.returnorderdetail;

import cn.sipin.sales.cloud.order.common.ReturnOrderDetailCommonVo;
import lombok.Data;

@Data
public class ReturnOrderDetailDto extends ReturnOrderDetailCommonVo {

    private Long id;

    private String orderDetailNo;

    /**
     * 是否赠品
     */
    private Integer isGift = 0;

    /**
     * 是否自提
     */
    private Integer isPickup;
}
