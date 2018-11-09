/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.service.front;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.sipin.cloud.order.client.fallback.front.CouponServiceFallback;
import cn.siyue.platform.base.ResponseData;
import io.swagger.annotations.ApiOperation;

@FeignClient(name = "order-service" , path = "/front/member-coupon", fallback = CouponServiceFallback.class)
public interface CouponService {

  @RequestMapping(value = "/{code}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseData index(
      @PathVariable(value = "code") String code);



  @RequestMapping(value = "/bind/{mobile}/{code}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseData bind(
      @PathVariable(value = "mobile") String mobile,
      @PathVariable(value = "code") String code
  );

}
