/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.controller.backend;


import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import cn.sipin.cloud.order.client.service.backend.SalesOrderBackendService;
import cn.sipin.cloud.order.client.utils.ExportOrderExcelUtils;
import cn.sipin.cloud.order.client.utils.JsonTransformer;
import cn.sipin.sales.cloud.order.request.backend.index.IndexOrdersRequest;
import cn.sipin.sales.cloud.order.response.backend.index.IndexOrdersResponse;
import cn.siyue.platform.base.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "经销商端_后台_销售订单管理接口")
@RequestMapping(path = "/backend/orders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class SalesOrderBackendController {


  private SalesOrderBackendService salesOrderBackendService;

  @Autowired
  public SalesOrderBackendController(SalesOrderBackendService salesOrderBackendService){
    this.salesOrderBackendService = salesOrderBackendService;
  }


  @ApiOperation(nickname = "salesOrderBackendIndexOrders", value = "后台获取销售订单列表", httpMethod = "GET")
  @GetMapping(value = "/index", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData index(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
      @Valid IndexOrdersRequest request
  ) {
     return salesOrderBackendService.backendIndex(page,size,request);
  }

  @ApiOperation(nickname = "salesOrderBackendDetailOrders",value = "获取指定销售订单的接口")
  @GetMapping("/detail/{no}")
  public ResponseData detail(@PathVariable String no) {
    return salesOrderBackendService.detail(no);
  }

  @ApiOperation(nickname = "salesOrderBackendExport", value = "后台导出销售订单", httpMethod = "GET")
  @GetMapping(value = "/exportExcel")
  public void exportExcel(@Valid IndexOrdersRequest request,HttpServletResponse res) {
    try{
      ResponseData responseData = salesOrderBackendService.exportExcel(request);
      List<IndexOrdersResponse> ordersList = JsonTransformer
          .getObjectMapper()
          .convertValue(responseData.getData(), new TypeReference<List<IndexOrdersResponse>>() {});
      ExportOrderExcelUtils.export(res,ordersList);
    } catch(Exception e){
      e.printStackTrace();
    }
  }
}
