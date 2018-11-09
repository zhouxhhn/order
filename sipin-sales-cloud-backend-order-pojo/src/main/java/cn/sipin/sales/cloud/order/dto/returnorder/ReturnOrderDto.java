package cn.sipin.sales.cloud.order.dto.returnorder;

import cn.sipin.sales.cloud.order.common.ReturnOrderCommonVo;
import lombok.Data;

@Data
public class ReturnOrderDto extends ReturnOrderCommonVo {
    private Long id;

    private String orderNo;
}
