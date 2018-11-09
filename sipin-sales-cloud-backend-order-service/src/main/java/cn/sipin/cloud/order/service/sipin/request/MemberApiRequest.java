package cn.sipin.cloud.order.service.sipin.request;

import org.springframework.http.HttpMethod;

import cn.sipin.cloud.order.service.sipin.response.MemberApiResponse;

/**
 * 会员信息请求
 *
 * @author Sipin Backend Development Team
 */
public class MemberApiRequest extends AbstractApiRequest<MemberApiResponse> {

  private String mobile;

  public MemberApiRequest(String mobile) {

    this.mobile = mobile;
  }

  @Override
  protected String path() {
    return String.format("/api/sales/member/%s", mobile);
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
  public Class<MemberApiResponse> responseClass() {
    return MemberApiResponse.class;
  }
}
