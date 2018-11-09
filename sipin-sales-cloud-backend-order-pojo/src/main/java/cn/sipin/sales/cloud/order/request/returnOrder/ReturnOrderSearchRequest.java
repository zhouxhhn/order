package cn.sipin.sales.cloud.order.request.returnOrder;

import cn.sipin.sales.cloud.order.common.PageRequest;
import lombok.Data;

import java.util.Map;

@Data
public class ReturnOrderSearchRequest extends PageRequest {

    private String beginDate;

    private String endDate;

    private String no;

    private Long id;

    private Long orderId;

    private Integer statusId;

}
