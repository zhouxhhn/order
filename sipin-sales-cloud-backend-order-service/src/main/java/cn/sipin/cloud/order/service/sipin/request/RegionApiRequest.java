/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.sipin.request;

import org.springframework.http.HttpMethod;

import cn.sipin.cloud.order.service.sipin.response.RegionApiResponse;

public class RegionApiRequest extends AbstractApiRequest<RegionApiResponse> {


  @Override protected String path() {
    return "/api/sales/region";
  }

  @Override protected HttpMethod method() {
    return HttpMethod.GET;
  }

  @Override protected String body() {
    return null;
  }

  @Override public Class<RegionApiResponse> responseClass() {
    return RegionApiResponse.class;
  }
}
