/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.controller.backend;

import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.type.TypeReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import cn.sipin.cloud.order.client.service.backend.PaymentBackendService;
import cn.sipin.cloud.order.client.utils.ExportOrderPaymentExcelUtils;
import cn.sipin.cloud.order.client.utils.ExportReturnOrderExcelUtils;
import cn.sipin.cloud.order.client.utils.JsonTransformer;
import cn.sipin.sales.cloud.order.request.payment.backend.IndexSalesPaymentRequest;
import cn.sipin.sales.cloud.order.request.payment.index.IndexPaymentRequest;
import cn.sipin.sales.cloud.order.request.returnOrder.IndexReturnOrdersRequest;
import cn.sipin.sales.cloud.order.response.backend.index.IndexSalesPaymentResponse;
import cn.sipin.sales.cloud.order.response.front.index.IndexPaymentResponse;
import cn.sipin.sales.cloud.order.response.returnOrder.IndexReturnOrdersResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/sales/backend/payment")
@Api(tags = "经销商_后台_销售资金流水")
public class PaymentBackendController {

  private PaymentBackendService paymentBackendService;

  @Autowired
  public PaymentBackendController(PaymentBackendService paymentBackendService) {
    this.paymentBackendService = paymentBackendService;
  }


  @ApiOperation(nickname = "salesPaymentIndex", value = "后台获取支付流水列表", httpMethod = "GET")
  @GetMapping(value = "/index", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData<Page<IndexSalesPaymentResponse>> backendIndex(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
      @Valid IndexSalesPaymentRequest request, BindingResult result
  ) {
    if (result.hasErrors()) {
      return new ResponseData<>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(), result.getFieldError().getDefaultMessage());
    }
    return paymentBackendService.index(page,size,request);
  }

  /**
   * 导出交易流水列表数据
   */
  @ApiOperation(nickname = "backendExportPaymentList", value = "导出交易流水列表数据")
  @GetMapping(value = "/exportExcel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public void exportExcel(IndexSalesPaymentRequest request, HttpServletResponse res) {
    try{
      ResponseData responseData = paymentBackendService.exportExcel(request);
      List<IndexSalesPaymentResponse> paymentResponseList = JsonTransformer
          .getObjectMapper()
          .convertValue(responseData.getData(), new TypeReference<List<IndexSalesPaymentResponse>>() {});
      ExportOrderPaymentExcelUtils.export(res, paymentResponseList);
    } catch(Exception e){
      e.printStackTrace();
    }
  }


}
