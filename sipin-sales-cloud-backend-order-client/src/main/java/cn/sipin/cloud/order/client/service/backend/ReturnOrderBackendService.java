/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.service.backend;

import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.sipin.cloud.order.client.fallback.backend.ReturnOrderBackendServiceFallback;
import cn.sipin.cloud.order.client.fallback.front.ReturnOrderServiceFallback;
import cn.sipin.sales.cloud.order.request.returnOrder.IndexReturnOrdersRequest;
import cn.sipin.sales.cloud.order.response.returnOrder.IndexReturnOrdersResponse;
import cn.siyue.platform.base.ResponseData;

@FeignClient(name = "order-service" , path = "/backend/return-order", fallback = ReturnOrderBackendServiceFallback.class)
public interface ReturnOrderBackendService {

  @RequestMapping(value = "/index", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData<Page<IndexReturnOrdersResponse>> index(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
      IndexReturnOrdersRequest request
  );


  @RequestMapping(value = "/{orderNo}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData<IndexReturnOrdersResponse> show(@PathVariable(value = "orderNo") String orderNo);

  @RequestMapping(value = "/exportExcel", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData exportExcel(IndexReturnOrdersRequest request);

}
