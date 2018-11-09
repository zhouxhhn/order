/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.sipin.request;

import org.springframework.http.HttpMethod;

import cn.sipin.cloud.order.service.sipin.response.CommonApiResponse;

/**
 * 恢复已核销优惠券
 */
public class ResumeCouponApiRequest extends AbstractApiRequest<CommonApiResponse> {

  private String code;

  public ResumeCouponApiRequest(String code) {
    this.code = code;
  }

  @Override protected String path() {
    return "/api/sales/coupon/resume/" + code;
  }

  @Override protected HttpMethod method() {
    return HttpMethod.POST;
  }

  @Override protected String body() {
    return null;
  }

  @Override public Class<CommonApiResponse> responseClass() {
    return CommonApiResponse.class;
  }
}
