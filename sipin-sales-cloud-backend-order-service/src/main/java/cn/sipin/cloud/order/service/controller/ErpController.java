/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;

import cn.sipin.cloud.order.service.config.ErpConfig;
import cn.sipin.cloud.order.service.contract.Loggable;
import cn.sipin.cloud.order.service.service.OrderExpressServiceContract;
import cn.sipin.cloud.order.service.service.OrdersServiceContract;
import cn.sipin.sales.cloud.order.constants.OrderStatus;
import cn.sipin.sales.cloud.order.pojo.Orders;
import cn.sipin.sales.cloud.order.request.erp.ErpOrderExpressRequest;
import cn.sipin.sales.cloud.order.request.erp.ErpOrdersRequest;
import cn.sipin.sales.cloud.order.request.erp.ErpRequest;
import cn.sipin.sales.cloud.order.request.erp.ErpUpdateOrderStatusRequest;
import cn.sipin.sales.cloud.order.response.ErpSalesOrderResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import cn.siyue.platform.exceptions.exception.RequestException;
import cn.siyue.platform.httplog.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "ERP获取订单接口")
@RequestMapping(path = "/erp/sales-orders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ErpController implements Loggable {

  private OrdersServiceContract ordersService;

  private OrderExpressServiceContract orderExpressService;
  private ErpConfig erpConfig;

  @Autowired
  public ErpController(OrdersServiceContract ordersService, OrderExpressServiceContract orderExpressService, ErpConfig erpConfig) {
    this.ordersService = ordersService;
    this.orderExpressService = orderExpressService;
    this.erpConfig = erpConfig;
  }

  @LogAnnotation
  @PostMapping(value = "")
  @ApiOperation(nickname = "getSalesOrdersForErp", value = "ERP获取门店销售单列表")
  public ResponseData index(
      @RequestParam(value = "sign") String sign,
      @RequestBody ErpOrdersRequest request
  ) {

    // 验证x-erp-token
//    HttpServletRequest httpServletRequest = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
//    if (!erpConfig.getXErpToken().equals(httpServletRequest.getHeader("X-Erp-Token"))) {
//      throw new RequestException(ResponseBackCode.ERROR_TOKEN_TIMEOUT_CODE.getValue(), ResponseBackCode.ERROR_TOKEN_TIMEOUT_CODE.getMessage());
//    }
//
//    // 验证签名
//    String rawSign = sign(request, erpConfig.getSecret());
//    if (!rawSign.equals(sign)) {
//      throw new RequestException(ResponseBackCode.ERROR_AUTH_FAIL.getValue(), ResponseBackCode.ERROR_AUTH_FAIL.getMessage() + "：sign验证失败");
//    }

    Page<ErpSalesOrderResponse> orderVoPage = new Page<>(request.getPage(), request.getPageSize());
    orderVoPage.setAsc(false);

    try {
      orderVoPage = ordersService.selectOrderForErpPage(orderVoPage, request);

      return ResponseData.build(
          ResponseBackCode.SUCCESS.getValue(),
          ResponseBackCode.SUCCESS.getMessage(),
          orderVoPage
      );
    } catch (Exception e) {
      e.printStackTrace();
      throw new RequestException(ResponseBackCode.ERROR_AUTH_FAIL.getValue(), e.getMessage());
    }
  }

  @LogAnnotation
  @PostMapping("/status/update")
  @ApiOperation(nickname = "erpUpdateSalesOrderStatus", value = "ERP更新订单状态")
  public ResponseData updateOrderStatus(
      @RequestParam(value = "sign") String sign,
      @RequestBody @Valid ErpUpdateOrderStatusRequest request
  ) {
    // 验证x-erp-token
    HttpServletRequest httpServletRequest = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    if (!erpConfig.getXErpToken().equals(httpServletRequest.getHeader("X-Erp-Token"))) {
      throw new RequestException(ResponseBackCode.ERROR_TOKEN_TIMEOUT_CODE.getValue(), ResponseBackCode.ERROR_TOKEN_TIMEOUT_CODE.getMessage());
    }

    // 验证签名
    String rawSign = sign(request, erpConfig.getSecret());

    if (!rawSign.equals(sign)) {
      throw new RequestException(ResponseBackCode.ERROR_AUTH_FAIL.getValue(), ResponseBackCode.ERROR_AUTH_FAIL.getMessage());
    }

    Orders orders = new Orders();

    orders.setNo(request.getOrderNo());
    orders = ordersService.selectOne(new EntityWrapper<>(orders));
    if (Objects.isNull(request.getOrderNo()) || Objects.isNull(orders) || Objects.isNull(orders.getId())) {
      throw new RequestException(ResponseBackCode.ERROR_PARAM_INVALID.getValue(), "订单号" + ResponseBackCode.ERROR_PARAM_INVALID.getMessage());
    }

    if (orders.getStatusId().compareTo(request.getTargertOrderStatus()) >= 0) {
      // 如果要更新的目标状态小于等于本地订单状态，说明已更新过了
      return ResponseData.build(
          ResponseBackCode.SUCCESS.getValue(),
          ResponseBackCode.SUCCESS.getMessage(),
          true
      );
    }

    if (orders.getStatusId().equals(request.getSourceOrderStatus())) {

      Orders tempOrder = new Orders();
      tempOrder.setId(orders.getId());
      tempOrder.setStatusId(request.getTargertOrderStatus());
      Boolean isSuccess = ordersService.updateById(tempOrder);
      logger().info("订单号为{}的订单，状态从{}更新为{}", request.getOrderNo(), OrderStatus.getEnum(request.getSourceOrderStatus()), OrderStatus.getEnum(request.getTargertOrderStatus()));

      if (isSuccess) {
        return ResponseData.build(
            ResponseBackCode.SUCCESS.getValue(),
            ResponseBackCode.SUCCESS.getMessage(),
            true
        );
      }
    } else {
      return ResponseData.build(
          ResponseBackCode.ERROR_UPDATE_FAIL.getValue(),
          ResponseBackCode.ERROR_UPDATE_FAIL.getMessage() + ": 请求数据SourceOrderStatus与本地数据库订单状态不一致",
          false
      );
    }

    return ResponseData.build(
        ResponseBackCode.ERROR_UPDATE_FAIL.getValue(),
        ResponseBackCode.ERROR_UPDATE_FAIL.getMessage(),
        false
    );
  }

  @LogAnnotation
  @PostMapping("/express/create")
  @ApiOperation(nickname = "createExpressForErp", value = "ERP推送物流信息")
  public ResponseData createExpress(
      @RequestParam(value = "sign") String sign,
      @RequestBody @Valid ErpOrderExpressRequest request
  ) {
    // 验证x-erp-token
    HttpServletRequest httpServletRequest = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    if (!erpConfig.getXErpToken().equals(httpServletRequest.getHeader("X-Erp-Token"))) {
      throw new RequestException(ResponseBackCode.ERROR_TOKEN_TIMEOUT_CODE.getValue(), ResponseBackCode.ERROR_TOKEN_TIMEOUT_CODE.getMessage());
    }

    // 验证签名
    String rawSign = sign(request, erpConfig.getSecret());

    if (!rawSign.equals(sign)) {
      throw new RequestException(ResponseBackCode.ERROR_AUTH_FAIL.getValue(), ResponseBackCode.ERROR_AUTH_FAIL.getMessage());
    }

    Orders orders = new Orders();
    orders.setNo(request.getOrderNo());
    orders = ordersService.selectOne(new EntityWrapper<>(orders));

    if (Objects.isNull(request.getOrderNo()) || Objects.isNull(orders) || Objects.isNull(orders.getId())) {
      throw new RequestException(ResponseBackCode.ERROR_PARAM_INVALID.getValue(), "订单号" + ResponseBackCode.ERROR_PARAM_INVALID.getMessage());
    }

    Boolean isSuccess = orderExpressService.create(orders, request);
    if (isSuccess) {
      return ResponseData.build(
          ResponseBackCode.SUCCESS.getValue(),
          ResponseBackCode.SUCCESS.getMessage(),
          true
      );
    }

    return ResponseData.build(
        ResponseBackCode.ERROR_CREATE_FAIL.getValue(),
        ResponseBackCode.ERROR_CREATE_FAIL.getMessage(),
        false
    );
  }


  private String sign(ErpRequest request, String secret) {
    StringBuilder sign = new StringBuilder(secret);
    Map params = request.toParams();
    Iterator paramsIterator = params.entrySet().iterator();

    String key;
    while (paramsIterator.hasNext()) {
      Entry entry = (Entry) paramsIterator.next();
      key = (String) entry.getKey();
      String value = (String) entry.getValue();
      if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
        sign.append(key).append(value);
      }
    }

    sign.append(secret);

    MessageDigest md5;
    try {
      md5 = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }

    Charset charset = Charset.forName("UTF-8");

    if (charset == null) {
      throw new RuntimeException("没有找到UTF-8字符集");
    }

    byte[] bytes = sign.toString().getBytes(charset);

    md5.update(bytes);

    String hexBinary = DatatypeConverter.printHexBinary(md5.digest());

    if (hexBinary == null) {
      throw new RuntimeException("hexBinary为空");
    }
    // 注意转大写
    return hexBinary.toUpperCase();
  }
}
