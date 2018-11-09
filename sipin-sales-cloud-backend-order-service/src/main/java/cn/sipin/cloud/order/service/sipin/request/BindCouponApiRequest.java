/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.sipin.request;

import org.springframework.http.HttpMethod;

import cn.sipin.cloud.order.service.sipin.response.CommonApiResponse;

/**
 * 绑定优惠券
 */
public class BindCouponApiRequest extends AbstractApiRequest<CommonApiResponse> {

  private String code;

  private String mobile;

  public BindCouponApiRequest(String code, String mobile) {

    this.code = code;
    this.mobile = mobile;
  }

  @Override protected String path() {
    return "/api/sales/coupon/" + this.code + "/bind/" + this.mobile;
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
