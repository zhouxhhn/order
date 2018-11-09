/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.controller.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.sipin.cloud.order.client.service.front.CouponService;
import cn.siyue.platform.base.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/front/member-coupon")
@Api(tags = "经销商_斯品会员卡券管理_前台API")
public class CouponController {

  private CouponService couponService;

  @Autowired
  public CouponController(CouponService couponService) {
    this.couponService = couponService;
  }

  @GetMapping(value = "/{code}")
  @ApiOperation(nickname = "frontGetMemberCoupon", value = "获取会员优惠券")
  public ResponseData index(
      @PathVariable(value = "code") String code
  ) {

    return couponService.index(code);
  }

  @PostMapping(value = "/bind/{mobile}/{code}")
  @ApiOperation(nickname = "frontBindCoupon", value = "会员绑定优惠券")
  public ResponseData bind(
      @PathVariable(value = "mobile") String mobile,
      @PathVariable(value = "code") String code
  ) {

    return couponService.bind(mobile, code);
  }
}
