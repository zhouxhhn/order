package cn.sipin.cloud.order.service.sipin.request;

import org.springframework.http.HttpMethod;

import cn.sipin.cloud.order.service.sipin.response.CouponApiResponse;

/**
 * 优惠券适用规则请求
 *
 * @author Sipin Backend Development Team
 */
public class CouponApiRequest extends AbstractApiRequest<CouponApiResponse> {

  private final String code;

  /**
   * 请求构造函数
   *
   */
  public CouponApiRequest(String code) {
    this.code = code;
  }

  @Override
  protected String path() {
    return "/api/sales/coupon/" + this.code;
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
  public Class<CouponApiResponse> responseClass() {
    return CouponApiResponse.class;
  }
}
