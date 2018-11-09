/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.controller.front;

import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import cn.sipin.cloud.order.client.service.front.ReturnOrderService;
import cn.sipin.sales.cloud.order.request.returnOrder.IndexReturnOrdersRequest;
import cn.sipin.sales.cloud.order.request.returnOrder.SalesReturnOrderRequest;
import cn.sipin.sales.cloud.order.response.returnOrder.IndexReturnOrdersResponse;
import cn.sipin.sales.cloud.order.response.returnOrder.ReturnOrderResponse;
import cn.siyue.platform.base.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "经销商_销售退货单管理_前台API")
@RequestMapping(path = "/front/return-order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ReturnOrderController {

  private ReturnOrderService returnOrderService;

  @Autowired
  public ReturnOrderController(ReturnOrderService returnOrderService) {
    this.returnOrderService = returnOrderService;
  }

  @ApiOperation(nickname = "frontCreateReturnOrder", value = "前台新建销售退货单")
  @PostMapping("/create")
  public ResponseData<ReturnOrderResponse> store(@RequestBody @Valid SalesReturnOrderRequest salesReturnOrderRequest) {

    return returnOrderService.store(salesReturnOrderRequest);
  }


  @ApiOperation(nickname = "frontAuditReturnOrder", value = "前台审核销售退货单")
  @PostMapping("/audit/{no}")
  public ResponseData<ReturnOrderResponse> audit(@PathVariable String no) {
    return returnOrderService.audit(no);
  }

  /**
   * 获取销售退货单列表
   */
  @ApiOperation(nickname = "frontGetReturnOrderList", value = "获取前台销售退货单列表")
  @GetMapping(value = "/index", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData<Page<IndexReturnOrdersResponse>> index(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
      IndexReturnOrdersRequest request
  ) {

    return returnOrderService.index(page, size, request);
  }

  @ApiOperation(nickname = "frontShowReturnOrder", value = "前台获取退货单详情")
  @GetMapping("/{orderNo}")
  public ResponseData<IndexReturnOrdersResponse> show(@PathVariable String orderNo){

    return returnOrderService.show(orderNo);

  }

  /**
   * 取消订单
   */
  @ApiOperation(nickname = "frontCancelReturnOrder", value = "前台取消退货单详情")
  @PutMapping(value = "/cancel/{no}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData cancelReturnOrder(@PathVariable String no) {

    return returnOrderService.cancel(no);
  }

}
