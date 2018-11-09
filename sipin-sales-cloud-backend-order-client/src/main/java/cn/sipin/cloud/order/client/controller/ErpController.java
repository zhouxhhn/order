/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import cn.sipin.cloud.order.client.service.ErpService;
import cn.sipin.sales.cloud.order.request.erp.ErpOrderExpressRequest;
import cn.sipin.sales.cloud.order.request.erp.ErpOrdersRequest;
import cn.sipin.sales.cloud.order.request.erp.ErpUpdateOrderStatusRequest;
import cn.siyue.platform.base.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "ERP获取销售订单接口")
@RequestMapping(path = "/erp/sales-orders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ErpController {

  private ErpService erpService;

  @Autowired
  public ErpController(ErpService erpService) {
    this.erpService = erpService;
  }

  @PostMapping(value = "")
  @ApiOperation(nickname = "getSalesOrdersForErp", value = "ERP获取门店销售单列表")
  public ResponseData index(
      @RequestParam(value = "sign") String sign,
      @RequestBody ErpOrdersRequest request
  ) {
    return erpService.index(sign, request);
  }

  @PostMapping("/status/update")
  @ApiOperation(nickname = "updateSalesOrderStatusForErp", value = "ERP更新订单状态")
  public ResponseData updateOrderStatus(
      @RequestParam(value = "sign") String sign,
      @RequestBody @Valid ErpUpdateOrderStatusRequest request
  ) {

    return erpService.updateOrderStatus(sign, request);
  }

  @PostMapping("/express/create")
  @ApiOperation(nickname = "createExpressForErp", value = "ERP推送物流信息")
  public ResponseData createExpress(
      @RequestParam(value = "sign") String sign,
      @RequestBody @Valid ErpOrderExpressRequest request
  ) {
    return erpService.createExpress(sign, request);
  }

}
