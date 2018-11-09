/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.controller.backend;

import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.type.TypeReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import cn.sipin.cloud.order.client.service.backend.ReturnOrderBackendService;
import cn.sipin.cloud.order.client.utils.ExportOrderExcelUtils;
import cn.sipin.cloud.order.client.utils.ExportReturnOrderExcelUtils;
import cn.sipin.cloud.order.client.utils.JsonTransformer;
import cn.sipin.sales.cloud.order.request.returnOrder.IndexReturnOrdersRequest;
import cn.sipin.sales.cloud.order.response.backend.index.IndexOrdersResponse;
import cn.sipin.sales.cloud.order.response.returnOrder.IndexReturnOrdersResponse;
import cn.siyue.platform.base.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "经销商端_后台_销售退货单管理接口")
@RequestMapping(path = "/backend/return-order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ReturnOrderBackendController {

  private ReturnOrderBackendService returnOrderBackendService;

  @Autowired
  public ReturnOrderBackendController(ReturnOrderBackendService returnOrderBackendService) {
    this.returnOrderBackendService = returnOrderBackendService;
  }

  /**
   * 获取销售退货单列表
   */
  @ApiOperation(nickname = "backendGetReturnOrderList", value = "获取后台销售退货单列表")
  @GetMapping(value = "/index", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData<Page<IndexReturnOrdersResponse>> index(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
      IndexReturnOrdersRequest request
  ) {

    return returnOrderBackendService.index(page, size, request);
  }

  @ApiOperation(nickname = "backendShowReturnOrder", value = "后台台获取退货单详情")
  @GetMapping("/{orderNo}")
  public ResponseData<IndexReturnOrdersResponse> show(@PathVariable String orderNo) {

    return returnOrderBackendService.show(orderNo);
  }

  /**
   * 获取销售退货单列表
   */
  @ApiOperation(nickname = "backendExportReturnOrderList", value = "导出销售退货单列表数据")
  @GetMapping(value = "/exportExcel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public void exportExcel(IndexReturnOrdersRequest request,HttpServletResponse res) {
    try{
      ResponseData responseData = returnOrderBackendService.exportExcel(request);
      List<IndexReturnOrdersResponse> returnOrdersList = JsonTransformer
          .getObjectMapper()
          .convertValue(responseData.getData(), new TypeReference<List<IndexReturnOrdersResponse>>() {});
      ExportReturnOrderExcelUtils.export(res, returnOrdersList);
    } catch(Exception e){
      e.printStackTrace();
    }
  }
}
