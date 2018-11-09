/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.controller.front;

import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import cn.sipin.cloud.order.client.service.RedisClusterService;
import cn.sipin.cloud.order.client.service.front.PaymentService;
import cn.sipin.cloud.order.client.service.front.SalesOrderService;
import cn.sipin.sales.cloud.order.request.payment.index.IndexPaymentRequest;
import cn.sipin.sales.cloud.order.request.payment.sum.SumPaymentRequest;
import cn.sipin.sales.cloud.order.response.SalesOrderResponse;
import cn.sipin.sales.cloud.order.response.front.index.IndexPaymentResponse;
import cn.sipin.sales.cloud.order.response.front.sum.SumPaymentResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/sales/front/payment")
@Api(tags = "经销商_前台_支付")
public class PaymentController {

  private PaymentService paymentService;

  @Autowired
  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }


  @ApiOperation(nickname = "salesPaymentIndex", value = "前台获取支付流水列表", httpMethod = "GET")
  @GetMapping(value = "/index", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData<Page<IndexPaymentResponse>> index(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
      @Valid IndexPaymentRequest request, BindingResult result
  ) {
    if (result.hasErrors()) {
      return new ResponseData<>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(), result.getFieldError().getDefaultMessage());
    }
    return paymentService.index(page,size,request);
  }

  @ApiOperation(nickname = "salesPaymentSum", value = "前台订单汇总", httpMethod = "GET")
  @GetMapping(value = "/sum", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData<SumPaymentResponse>  sum(@Valid SumPaymentRequest request, BindingResult result
  ) {
    if (result.hasErrors()) {
      return new ResponseData<>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(), result.getFieldError().getDefaultMessage());
    }
    return paymentService.sum(request);
  }
}
