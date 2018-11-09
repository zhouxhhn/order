/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.service.front;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.sipin.cloud.order.client.fallback.front.MemberServiceFallback;
import cn.siyue.platform.base.ResponseData;

@FeignClient(name = "order-service" , path = "/front/member", fallback = MemberServiceFallback.class)
public interface MemberService {

  @RequestMapping(value = "/info/{mobile}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData getMember(@PathVariable(value = "mobile") String mobile);
}
