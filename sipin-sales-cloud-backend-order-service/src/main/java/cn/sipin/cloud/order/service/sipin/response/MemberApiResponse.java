package cn.sipin.cloud.order.service.sipin.response;


import cn.sipin.cloud.order.service.sipin.vo.member.MemberApi;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员信息接口响应
 *
 * @author Sipin Backend Development Team
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MemberApiResponse extends AbstractApiResponse {

  private MemberApi data;
}
