/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.service.front;

import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import cn.sipin.cloud.order.client.fallback.front.PaymentServiceFallback;
import cn.sipin.sales.cloud.order.request.payment.index.IndexPaymentRequest;
import cn.sipin.sales.cloud.order.request.payment.sum.SumPaymentRequest;
import cn.sipin.sales.cloud.order.response.front.index.IndexPaymentResponse;
import cn.sipin.sales.cloud.order.response.front.sum.SumPaymentResponse;
import cn.siyue.platform.base.ResponseData;

@FeignClient(name = "order-service", path = "/sales/front/payment", fallback = PaymentServiceFallback.class)
public interface PaymentService {

  @RequestMapping(value = "/index", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData<Page<IndexPaymentResponse>> index(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                                 IndexPaymentRequest request);

  @RequestMapping(value = "/sum", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData<SumPaymentResponse> sum(SumPaymentRequest request);
}
