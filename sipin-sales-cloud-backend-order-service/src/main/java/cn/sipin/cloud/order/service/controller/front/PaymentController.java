package cn.sipin.cloud.order.service.controller.front;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cn.sipin.cloud.order.service.service.PaymentServiceContract;
import cn.sipin.sales.cloud.order.request.payment.index.IndexPaymentRequest;
import cn.sipin.sales.cloud.order.request.payment.sum.SumPaymentRequest;
import cn.sipin.sales.cloud.order.response.front.index.IndexPaymentResponse;
import cn.sipin.sales.cloud.order.response.front.sum.SumPaymentResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.httplog.annotation.LogAnnotation;

/**
 * 支付流水信息表 前端控制器
 */
@RestController
@RequestMapping(path = "/sales/front/payment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PaymentController {

  private PaymentServiceContract paymentService;

  @Autowired
  public PaymentController(PaymentServiceContract paymentService) {
      this.paymentService = paymentService;
  }

  /**
   * 前台获取流水列表
   */
  @LogAnnotation
  @PostMapping(value = "/index", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData<Page<IndexPaymentResponse>> index(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
      @RequestBody IndexPaymentRequest request) {
      return paymentService.index(page,size,request);
  }

  /**
   * 汇总
   */
  @LogAnnotation
  @PostMapping(value = "/sum", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData<SumPaymentResponse> sum(@RequestBody SumPaymentRequest request) {
    return paymentService.sum(request);
  }
}

