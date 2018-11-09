/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.service.backend;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import cn.sipin.cloud.order.client.fallback.backend.SalesOrderBackendServiceFallback;
import cn.sipin.sales.cloud.order.request.backend.index.IndexOrdersRequest;
import cn.siyue.platform.base.ResponseData;

@FeignClient(name = "order-service", path = "/backend/orders", fallback = SalesOrderBackendServiceFallback.class)
public interface SalesOrderBackendService {

  @RequestMapping(value = "/index", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
   ResponseData backendIndex( @RequestParam(value = "page") int page,@RequestParam(value = "size") int size,
      IndexOrdersRequest request);

  @RequestMapping(value = "/detail/{no}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData detail(@PathVariable(value = "no") String no);

  @RequestMapping(value = "/exportExcel", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData exportExcel(IndexOrdersRequest request);

}
