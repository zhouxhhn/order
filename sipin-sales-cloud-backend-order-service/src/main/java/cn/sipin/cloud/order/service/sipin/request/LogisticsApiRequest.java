package cn.sipin.cloud.order.service.sipin.request;


import org.springframework.http.HttpMethod;

import cn.sipin.cloud.order.service.sipin.response.LogisticsApiResponse;

/**
 * 订单物流信息请求
 *
 * @author Sipin Backend Development Team
 */
public class LogisticsApiRequest extends AbstractApiRequest<LogisticsApiResponse> {

  private String orderNo;

  public LogisticsApiRequest(String orderNo) {
    this.orderNo = orderNo;
  }

  @Override
  protected String path() {
    return "/api/sales/logistics/" + orderNo;
  }

  @Override
  protected HttpMethod method() {
    return HttpMethod.GET;
  }

  @Override
  protected String body() {
    return null;
  }

  @Override
  public Class<LogisticsApiResponse> responseClass() {
    return LogisticsApiResponse.class;
  }
}
