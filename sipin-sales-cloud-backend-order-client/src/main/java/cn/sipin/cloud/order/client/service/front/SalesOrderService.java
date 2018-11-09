/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.service.front;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import cn.sipin.cloud.order.client.fallback.front.SalesOrderServiceFallback;
import cn.sipin.sales.cloud.order.request.SalesOrderRequest;
import cn.sipin.sales.cloud.order.request.backend.index.IndexOrdersRequest;
import cn.sipin.sales.cloud.order.request.confirmPayment.ConfirmPaymentRequest;
import cn.sipin.sales.cloud.order.request.editNotes.EditNotesRequest;
import cn.sipin.sales.cloud.order.request.wholeDiscount.WholeDiscountRequest;
import cn.sipin.sales.cloud.order.response.SalesOrderResponse;
import cn.siyue.platform.base.ResponseData;

@FeignClient(name = "order-service", path = "/front/orders", fallback = SalesOrderServiceFallback.class)
public interface SalesOrderService {

  @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseData<SalesOrderResponse> store(SalesOrderRequest salesOrderRequest);

  @RequestMapping(value = "/index", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData index(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                            IndexOrdersRequest request);

  @RequestMapping(value = "/detail/{no}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData detail(@PathVariable(value = "no") String no);

  @RequestMapping(value = "/wholeDiscount/{no}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData wholeDiscount(@PathVariable(value = "no") String no,WholeDiscountRequest wholeDiscountRequest);


  @RequestMapping(value = "/confirmPayment/{no}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData confirmPayment(@PathVariable(value = "no") String no,ConfirmPaymentRequest request);


  @RequestMapping(value = "/editNotes/{no}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData editNotes(@PathVariable(value = "no") String no,EditNotesRequest request);


  @RequestMapping(value = "/cancelOrder/{no}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData cancelOrder(@PathVariable(value = "no") String no);


  @RequestMapping(value = "/confirmComplete/{no}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData confirmComplete(@PathVariable(value = "no") String no);

  @RequestMapping(value = "/getPaymentCode/{no}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData getPaymentCode(@PathVariable(value = "no") String no);

}
