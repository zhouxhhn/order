/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.service.front;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.sipin.cloud.order.client.fallback.front.MemberConsigneeServiceFallback;
import cn.sipin.sales.cloud.order.request.MemberAddressRequest;
import cn.siyue.platform.base.ResponseData;

@FeignClient(name = "order-service" , path = "/front/member-consignee", fallback = MemberConsigneeServiceFallback.class)
public interface MemberConsigneeService {


  @RequestMapping(value = "/region", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseData getRegion();


  @RequestMapping(value = "/create/{mobile}/address", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseData createConsignee(
      @PathVariable(value = "mobile") String mobile,
      @RequestBody MemberAddressRequest request
  );


  @RequestMapping(value = "/update/{mobile}/address/{addressId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseData updateConsignee(
      @PathVariable(value = "mobile") String mobile,
      @PathVariable(value = "addressId") String addressId,
      @RequestBody MemberAddressRequest request
  );
}
