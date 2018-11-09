package cn.sipin.cloud.order.service.sipin.response;


import cn.sipin.cloud.order.service.sipin.vo.logistics.LogisticsApiData;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单物流信息请求
 *
 * @author Sipin Backend Development Team
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LogisticsApiResponse extends AbstractApiResponse {

  private LogisticsApiData[] data;
}
