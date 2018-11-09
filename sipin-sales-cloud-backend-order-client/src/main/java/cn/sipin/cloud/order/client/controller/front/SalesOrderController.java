/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.controller.front;

import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import cn.sipin.cloud.order.client.service.RedisClusterService;
import cn.sipin.cloud.order.client.service.front.SalesOrderService;
import cn.sipin.sales.cloud.order.constants.RedisConstants;
import cn.sipin.sales.cloud.order.request.SalesOrderRequest;
import cn.sipin.sales.cloud.order.request.backend.index.IndexOrdersRequest;
import cn.sipin.sales.cloud.order.request.confirmPayment.ConfirmPaymentRequest;
import cn.sipin.sales.cloud.order.request.editNotes.EditNotesRequest;
import cn.sipin.sales.cloud.order.request.wholeDiscount.WholeDiscountRequest;
import cn.sipin.sales.cloud.order.response.SalesOrderResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/front/orders")
@Api(tags = "经销商_销售订单管理_前台API")
public class SalesOrderController {

  private RedisClusterService redisService;

  private SalesOrderService orderService;

  @Autowired
  public SalesOrderController(RedisClusterService redisService, SalesOrderService orderService) {
    this.redisService = redisService;
    this.orderService = orderService;
  }

  @ApiOperation(nickname = "frontPurchaseOrderList", value = "前台获取采购订单列表", httpMethod = "GET")
  @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData<Page<SalesOrderResponse>> index(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size
  ) {

    try {

      Page<SalesOrderResponse> pageVo = new Page<>(page, size);

      return new ResponseData<Page<SalesOrderResponse>>(
          ResponseBackCode.SUCCESS.getValue(),
          ResponseBackCode.SUCCESS.getMessage(),
          pageVo
      );
    } catch (Exception e) {

      return new ResponseData<Page<SalesOrderResponse>>(
          ResponseBackCode.ERROR_FAIL.getValue(),
          ResponseBackCode.ERROR_FAIL.getMessage()
      );
    }
  }

  @ApiOperation(nickname = "frontCreateSalesOrder", value = "前台创建销售订单")
  @PostMapping("/create")
  public ResponseData<SalesOrderResponse> store(
      @RequestBody SalesOrderRequest request
  ) {

    String sessionKey = RedisConstants.SALES_ORDER_UNIQUE + request.getSessionKey();
    if (redisService.setnx(sessionKey, RedisConstants.SALES_ORDER_UNIQUE_VALUE)) {
      redisService.expire(sessionKey, 1);
    } else {
      return new ResponseData<SalesOrderResponse>(
          ResponseBackCode.ERROR_CREATE_FAIL.getValue(),
          "创建失败：重复请求"
      );
    }

    return orderService.store(request);
  }

  @ApiOperation(nickname = "indexSalesOrder", value = "获取销售订单列表", httpMethod = "GET")
  @GetMapping(value = "/index", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData index(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
      @Valid IndexOrdersRequest request, BindingResult result
  ) {
    if (result.hasErrors()) {
      return new ResponseData<>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),result.getFieldError().getDefaultMessage());
    }
    return orderService.index(page,size,request);
  }

  @ApiOperation(nickname = "detailSalesOrder",value = "获取指定销售订单的接口")
  @GetMapping("/detail/{no}")
  public ResponseData detail(@PathVariable String no) {
    return orderService.detail(no);
  }

  /**
   * 整单优惠
   */
  @ApiOperation(nickname = "wholeDiscountSalesOrder",value = "整单优惠的接口")
  @PutMapping("/wholeDiscount/{no}")
  public ResponseData wholeDiscount(@PathVariable String no,@RequestBody @Valid  WholeDiscountRequest request, BindingResult result
  ) {
    if (result.hasErrors()) {
      return new ResponseData<>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),result.getFieldError().getDefaultMessage());
    }
    return orderService.wholeDiscount(no,request);
  }

  /**
   * 确认收银
   */
  @ApiOperation(nickname = "confirmPaymentSalesOrder",value = "确认收银的接口")
  @PostMapping("/confirmPayment/{no}")
  public ResponseData confirmPayment(@PathVariable String no,@RequestBody @Valid ConfirmPaymentRequest request, BindingResult result
  ) {
    if (result.hasErrors()) {
      return new ResponseData<>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),result.getFieldError().getDefaultMessage());
    }
    return orderService.confirmPayment(no,request);
  }

  /**
   * 备注
   */
  @ApiOperation(nickname = "editNotesSalesOrder",value = "修改备注的接口")
  @PutMapping("/editNotes/{no}")
  public ResponseData editNotes(@PathVariable String no,@RequestBody @Valid EditNotesRequest request, BindingResult result
  ) {
    if (result.hasErrors()) {
      return new ResponseData<>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),result.getFieldError().getDefaultMessage());
    }
     return orderService.editNotes(no,request);
  }

  /**
   * 取消订单
   */
  @ApiOperation(nickname = "cancelOrderSalesOrder",value = "取消订单的接口")
  @PutMapping("/cancelOrder/{no}")
  public ResponseData cancelOrder(@PathVariable String no) {
     return orderService.cancelOrder(no);
  }

  /**
   * 确认完成
   */
  @ApiOperation(nickname = "confirmCompleteSalesOrder",value = "确认完成订单的接口")
  @PutMapping("/confirmComplete/{no}")
  public ResponseData confirmComplete(@PathVariable String no) {
     return orderService.confirmComplete(no);
  }

  /**
   * 获取支付交易码
   */
  @ApiOperation(nickname = "getPaymentCodeSalesOrder",value = "获取支付交易码的接口")
  @GetMapping("/getPaymentCode/{no}")
  public ResponseData getPaymentCode(@PathVariable String no) {
    return orderService.getPaymentCode(no);
  }

}
