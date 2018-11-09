package cn.sipin.cloud.order.service.sipin.vo.coupon;

import cn.sipin.cloud.order.service.sipin.constant.CouponStatusEnum;
import lombok.Data;

/**
 * 优惠券响应返回实体
 *
 * @author Sipin Backend Development Team
 */
@Data
public class CouponApi {

  /** 优惠码 */
  private String code;

  /** 优惠券适用会员信息 */
  private CouponApiMember member;

  /** 优惠券规则 */
  private CouponApiTask task;

  /** 优惠券状态 */
  private CouponStatusEnum status;
}
