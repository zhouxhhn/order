/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.controller.backend;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import cn.sipin.cloud.order.service.service.OrdersServiceContract;
import cn.sipin.sales.cloud.order.request.backend.index.IndexOrdersRequest;
import cn.sipin.sales.cloud.order.response.backend.index.IndexOrdersResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import cn.siyue.platform.httplog.annotation.LogAnnotation;

@RequestMapping(path = "/backend/orders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class SalesOrderBackendController {

  private OrdersServiceContract ordersService;

  @Autowired
  public SalesOrderBackendController(OrdersServiceContract ordersService){
    this.ordersService = ordersService;
  }

  /**
   * 后台获取销售订单列表
   */
  @LogAnnotation
  @PostMapping(value = "/index", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData<Page<IndexOrdersResponse>> index(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
      @RequestBody IndexOrdersRequest request
  ) {
    return ordersService.backendIndex(page,size,request);
  }

  /**
   * 后台获取销售订单详情数据
   */
  @LogAnnotation
  @GetMapping(value = "/detail/{no}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData detail(@PathVariable String no) {
    return ordersService.backendDetail(no);
  }


  /**
   * 后台导出销售订单列表
   */
  @LogAnnotation
  @PostMapping(value = "/exportExcel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData exportExcel(@RequestBody IndexOrdersRequest request) {
     return ordersService.exportExcel(request);

  }

}
