/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.controller.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.sipin.cloud.order.service.sipin.client.SipinClient;
import cn.sipin.cloud.order.service.sipin.request.BindCouponApiRequest;
import cn.sipin.cloud.order.service.sipin.request.CouponApiRequest;
import cn.sipin.cloud.order.service.sipin.response.CommonApiResponse;
import cn.sipin.cloud.order.service.sipin.response.CouponApiResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.httplog.annotation.LogAnnotation;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/front/member-coupon")
public class CouponController {

  private SipinClient sipinClient;

  @Autowired
  public CouponController(SipinClient sipinClient) {
    this.sipinClient = sipinClient;
  }

  @LogAnnotation
  @GetMapping(value = "/{code}")
  @ApiOperation(nickname = "frontGetMemberCoupon", value = "获取会员优惠券")
  public ResponseData index(
      @PathVariable(value = "code") String code
  ) {

    CouponApiRequest request = new CouponApiRequest(code);

    CouponApiResponse response = sipinClient.execute(request);

    return ResponseData.build(response.getCode(), response.getMsg(), response.getData());
  }

  @LogAnnotation
  @PostMapping(value = "/bind/{mobile}/{code}")
  @ApiOperation(nickname = "frontBindCoupon", value = "会员绑定优惠券")
  public ResponseData bind(
      @PathVariable(value = "mobile") String mobile,
      @PathVariable(value = "code") String code
  ) {

    BindCouponApiRequest request = new BindCouponApiRequest(code, mobile);
    CommonApiResponse response = sipinClient.execute(request);

    return ResponseData.build(response.getCode(), response.getMsg(), response.getData());
  }
}
