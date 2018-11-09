/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.sipin.cloud.order.client.fallback.ErpServiceFallBack;
import cn.sipin.sales.cloud.order.request.erp.ErpOrderExpressRequest;
import cn.sipin.sales.cloud.order.request.erp.ErpOrdersRequest;
import cn.sipin.sales.cloud.order.request.erp.ErpUpdateOrderStatusRequest;
import cn.siyue.platform.base.ResponseData;

@FeignClient(name = "order-service", fallback = ErpServiceFallBack.class)
public interface ErpService {

  @RequestMapping(value = "/erp/sales-orders", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ResponseData index(
      @RequestParam(value = "sign") String sign,
      ErpOrdersRequest request
  );

  @RequestMapping(value = "/erp/sales-orders/status/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ResponseData updateOrderStatus(
      @RequestParam(value = "sign") String sign,
      ErpUpdateOrderStatusRequest request);


  @RequestMapping(value = "/erp/sales-orders/express/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ResponseData createExpress(
      @RequestParam(value = "sign") String sign,
      ErpOrderExpressRequest request
  );
}
