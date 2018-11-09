/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.sipin.request;

import org.springframework.http.HttpMethod;

import java.util.Objects;

import cn.sipin.cloud.order.service.sipin.response.CommonApiResponse;
import cn.sipin.cloud.order.service.sipin.vo.member.MemberConsigneeApiData;
import cn.sipin.cloud.order.service.util.JsonTransformer;

public class ConsigneeApiRequest extends AbstractApiRequest<CommonApiResponse> {

  private String mobile;

  private String addressId;

  private MemberConsigneeApiData data;

  /**
   * post: true;  put: false
   */
  private Boolean isPost = true;

  /**
   * 创建会员地址请求构造函数
   */
  public ConsigneeApiRequest(String mobile, MemberConsigneeApiData data) {
    this.mobile = mobile;
    this.data = data;
  }

  /**
   * 更新会员地址请求构造函数
   */
  public ConsigneeApiRequest(String mobile, String addressId, MemberConsigneeApiData data) {

    this.mobile = mobile;
    this.addressId = addressId;
    this.data = data;
    this.isPost = false;
  }

  /**
   * 创建地址：/api/sales/member/{mobile}/address ; 更新地址：/api/sales/member/{mobile}/address/{addressId}
   */
  @Override protected String path() {
    String path = String.format("/api/sales/member/%s/address", this.mobile);
    if (!Objects.isNull(addressId)) {
      path = path + "/" + addressId;
    }
    return path;
  }

  @Override protected HttpMethod method() {
    if (this.isPost) {
      return HttpMethod.POST;
    } else {
      return HttpMethod.PUT;
    }
  }

  @Override protected String body() {
    return JsonTransformer.toJson(this.data);
  }

  @Override public Class<CommonApiResponse> responseClass() {
    return CommonApiResponse.class;
  }
}
