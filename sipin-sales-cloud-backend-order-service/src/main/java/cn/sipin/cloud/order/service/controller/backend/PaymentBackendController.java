package cn.sipin.cloud.order.service.controller.backend;

import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.sipin.cloud.order.service.service.PaymentServiceContract;
import cn.sipin.sales.cloud.order.request.payment.backend.IndexSalesPaymentRequest;
import cn.sipin.sales.cloud.order.request.payment.index.IndexPaymentRequest;
import cn.sipin.sales.cloud.order.request.payment.sum.SumPaymentRequest;
import cn.sipin.sales.cloud.order.response.backend.index.IndexSalesPaymentResponse;
import cn.sipin.sales.cloud.order.response.front.index.IndexPaymentResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import cn.siyue.platform.httplog.annotation.LogAnnotation;

/**
 * 支付流水信息表 前端控制器
 */
@RestController
@RequestMapping(path = "/sales/backend/payment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PaymentBackendController {

  private PaymentServiceContract paymentService;

  @Autowired
  public PaymentBackendController(PaymentServiceContract paymentService) {
      this.paymentService = paymentService;
  }


  /**
   * 后台获取流水列表
   */
  @LogAnnotation
  @PostMapping(value = "/index", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData<Page<IndexSalesPaymentResponse>> backendIndex(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
      @RequestBody IndexSalesPaymentRequest request) {
    return paymentService.backIndex(page,size,request,false);
  }

  /**
   * 后台导出流水列表
   */
  @LogAnnotation
  @PostMapping(value = "/exportExcel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData exportExcel( @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                   @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
                                   @RequestBody IndexSalesPaymentRequest request) {
    ResponseData responseData = paymentService.backIndex(page,size,request,true);
    Page<IndexSalesPaymentResponse> responsePage = (Page) responseData.getData();
    return  ResponseData.build(ResponseBackCode.SUCCESS.getValue(), ResponseBackCode.SUCCESS.getMessage(), responsePage.getRecords());
  }

}

