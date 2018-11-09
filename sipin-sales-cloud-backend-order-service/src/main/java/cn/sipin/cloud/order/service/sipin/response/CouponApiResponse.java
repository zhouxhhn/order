package cn.sipin.cloud.order.service.sipin.response;


import cn.sipin.cloud.order.service.sipin.vo.coupon.CouponApi;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 优惠券适用规则请求响应
 *
 * @author Sipin Backend Development Team
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CouponApiResponse extends AbstractApiResponse {

  private CouponApi data;
}
