/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.sipin.request;

import org.springframework.http.HttpMethod;

import cn.sipin.cloud.order.service.sipin.response.CommonApiResponse;

/**
 * 核销优惠券
 */
public class ConsumeCouponApiRequest extends AbstractApiRequest<CommonApiResponse> {

  private String code;

  public ConsumeCouponApiRequest(String code) {
    this.code = code;
  }

  @Override protected String path() {
    return "/api/sales/coupon/consume/" + code;
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
