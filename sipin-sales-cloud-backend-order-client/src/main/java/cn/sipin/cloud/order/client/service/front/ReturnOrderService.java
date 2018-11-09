/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.service.front;

import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.sipin.cloud.order.client.fallback.front.ReturnOrderServiceFallback;
import cn.sipin.sales.cloud.order.request.returnOrder.IndexReturnOrdersRequest;
import cn.sipin.sales.cloud.order.request.returnOrder.SalesReturnOrderRequest;
import cn.sipin.sales.cloud.order.response.returnOrder.IndexReturnOrdersResponse;
import cn.sipin.sales.cloud.order.response.returnOrder.ReturnOrderResponse;
import cn.siyue.platform.base.ResponseData;

@FeignClient(name = "order-service" , path = "/front/return-order", fallback = ReturnOrderServiceFallback.class)
public interface ReturnOrderService {
  @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData<ReturnOrderResponse> store(SalesReturnOrderRequest salesReturnOrderRequest);

  @RequestMapping(value = "/audit/{no}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData<ReturnOrderResponse> audit(@PathVariable(value = "no") String no);

  @RequestMapping(value = "/index", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  ResponseData<Page<IndexReturnOrdersResponse>> index(
      @RequestParam(value = "page") Integer page,
      @RequestParam(value = "size") Integer size,
      IndexReturnOrdersRequest request);

  @RequestMapping(value = "/{orderNo}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData<IndexReturnOrdersResponse> show(@PathVariable(value = "orderNo") String orderNo);

  @RequestMapping(value = "/cancel/{orderNo}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData cancel(@PathVariable(value = "orderNo") String orderNo);
}
