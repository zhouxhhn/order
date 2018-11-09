/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.service.backend;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import cn.sipin.cloud.order.client.fallback.backend.PaymentBackendServiceFallback;
import cn.sipin.sales.cloud.order.request.payment.backend.IndexSalesPaymentRequest;
import cn.sipin.sales.cloud.order.response.backend.index.IndexSalesPaymentResponse;
import cn.siyue.platform.base.ResponseData;

@FeignClient(name = "order-service", path = "/sales/backend/payment", fallback = PaymentBackendServiceFallback.class)
public interface PaymentBackendService {

  @RequestMapping(value = "/index", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData<Page<IndexSalesPaymentResponse>> index(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,IndexSalesPaymentRequest request );

  @RequestMapping(value = "/exportExcel", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData exportExcel(IndexSalesPaymentRequest request);

}
