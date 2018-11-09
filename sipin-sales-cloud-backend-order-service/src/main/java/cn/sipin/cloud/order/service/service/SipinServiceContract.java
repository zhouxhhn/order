/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.service;

import java.util.concurrent.Future;

import cn.sipin.cloud.order.service.sipin.vo.member.MemberApi;

public interface SipinServiceContract {

  MemberApi getMember(String mobile);

  /**
   * 核销优惠券
   */
  Future<Boolean> cancelCoupon(String code);

  /**
   * 恢复优惠券
   */
  Future<Boolean> resumeCoupon(String code);

  /**
   * 核销积分
   */
  Future<Boolean> updatePoint(String mobile, int point, byte typeId, String orderNo);
}
