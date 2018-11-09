package cn.sipin.cloud.order.service.sipin.vo.member;

import cn.sipin.cloud.order.service.sipin.constant.CouponStatusEnum;
import cn.sipin.cloud.order.service.sipin.vo.coupon.CouponApiTask;
import lombok.Data;

/**
 * 会员优惠券信息
 *
 * @author Sipin Backend Development Team
 */
@Data
public class MemberApiCoupon {

  /** 优惠券代码 */
  private String code;

  /** 优惠券详情 */
  private CouponApiTask task;

  /** 优惠券状态 */
  private CouponStatusEnum status;
}
