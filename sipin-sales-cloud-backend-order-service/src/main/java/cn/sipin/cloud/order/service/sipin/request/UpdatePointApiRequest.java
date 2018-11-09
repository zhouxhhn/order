/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.sipin.request;

import org.springframework.http.HttpMethod;

import java.util.TreeMap;

import cn.sipin.cloud.order.service.sipin.response.UpdatePointApiResponse;
import cn.sipin.cloud.order.service.util.JsonTransformer;

public class UpdatePointApiRequest extends AbstractApiRequest<UpdatePointApiResponse> {

  private String memberMobile;

  private Integer point;

  /**
   * 积分调整类型：5为调减、24为调增
   */
  private byte typeId;

  /**
   * 唯一订单号+积分类型，防止OKhttp3重试或者微服务重试而引起重复扣积分
   */
  private String sessionKey;

  /**
   * 请求构造函数
   */
  public UpdatePointApiRequest(String memberMobile, Integer point, byte typeId, String orderNo) {
    this.memberMobile = memberMobile;
    this.point = point;
    this.typeId = typeId;
    this.sessionKey = orderNo + Byte.toString(typeId);
  }

  @Override protected String path() {
    return "/api/sales/member/point";
  }

  @Override protected HttpMethod method() {
    return HttpMethod.POST;
  }

  @Override protected String body() {

    TreeMap<String, Object> treeMap = new TreeMap<>();
    treeMap.put("member_mobile", this.memberMobile);

    treeMap.put("point", this.point);

    treeMap.put("type_id", this.typeId);

    treeMap.put("session_key", this.sessionKey);

    return JsonTransformer.toJson(treeMap);
  }

  @Override public Class<UpdatePointApiResponse> responseClass() {
    return UpdatePointApiResponse.class;
  }
}
