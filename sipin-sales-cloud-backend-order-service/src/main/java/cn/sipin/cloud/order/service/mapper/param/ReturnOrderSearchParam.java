package cn.sipin.cloud.order.service.mapper.param;

import lombok.Data;

@Data
public class ReturnOrderSearchParam {

    private String beginDate;

    private String endDate;

    private String no;

    private Long id;

    private Long orderId;

    private Integer statusId;
}
