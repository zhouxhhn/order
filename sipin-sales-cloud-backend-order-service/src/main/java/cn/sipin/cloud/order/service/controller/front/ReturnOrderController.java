/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.controller.front;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.beans.BeanUtils;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import cn.sipin.cloud.order.service.config.ErpConfig;
import cn.sipin.cloud.order.service.feign.impl.SalesUserServiceImpl;
import cn.sipin.cloud.order.service.service.OrderDetailServiceContract;
import cn.sipin.cloud.order.service.service.OrdersServiceContract;
import cn.sipin.cloud.order.service.service.PointServiceContract;
import cn.sipin.cloud.order.service.service.ReturnOrderDetailServiceContract;
import cn.sipin.cloud.order.service.service.ReturnOrderServiceContract;
import cn.sipin.sales.cloud.order.constants.AuditStatus;
import cn.sipin.sales.cloud.order.constants.OrderStatus;
import cn.sipin.sales.cloud.order.constants.RefundType;
import cn.sipin.sales.cloud.order.pojo.OrderDetail;
import cn.sipin.sales.cloud.order.pojo.Orders;
import cn.sipin.sales.cloud.order.pojo.ReturnOrder;
import cn.sipin.sales.cloud.order.request.returnOrder.IndexReturnOrdersRequest;
import cn.sipin.sales.cloud.order.request.returnOrder.SalesReturnOrderRequest;
import cn.sipin.sales.cloud.order.request.vo.ReturnSkuDetailVo;
import cn.sipin.sales.cloud.order.response.AgencyCodeResponse;
import cn.sipin.sales.cloud.order.response.returnOrder.IndexReturnOrdersResponse;
import cn.sipin.sales.cloud.order.response.returnOrder.ReturnOrderResponse;
import cn.sipin.sales.cloud.order.response.vo.ReturnOrderDetailVo;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import cn.siyue.platform.httplog.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "前台销售退货单")
@RequestMapping(path = "/front/return-order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ReturnOrderController {

  private ReturnOrderServiceContract returnOrderService;

  private ReturnOrderDetailServiceContract returnOrderDetailService;

  private OrdersServiceContract ordersService;

  private OrderDetailServiceContract orderDetailService;

  private SalesUserServiceImpl salesUserService;

  private ErpConfig erpConfig;

  private PointServiceContract pointService;

  @Autowired
  public ReturnOrderController(
      ReturnOrderServiceContract returnOrderService, ReturnOrderDetailServiceContract returnOrderDetailService,
      OrdersServiceContract ordersService,
      OrderDetailServiceContract orderDetailService,
      SalesUserServiceImpl salesUserService,
      ErpConfig erpConfig,
      PointServiceContract pointService
  ) {
    this.returnOrderService = returnOrderService;
    this.returnOrderDetailService = returnOrderDetailService;
    this.ordersService = ordersService;
    this.orderDetailService = orderDetailService;
    this.salesUserService = salesUserService;
    this.erpConfig = erpConfig;
    this.pointService = pointService;
  }

  @LogAnnotation
  @ApiOperation(nickname = "frontCreateReturnOrder", value = "前台新建销售退货单")
  @PostMapping("/create")
  public ResponseData<ReturnOrderResponse> store(@RequestBody SalesReturnOrderRequest salesReturnOrderRequest) {
    AgencyCodeResponse agencyCodeResponse = salesUserService.getUserByToken();

    Orders orders = ordersService.setAndGetOwnOrders(salesReturnOrderRequest.getOrderNo());

    if (Objects.isNull(orders)) {
      return new ResponseData<ReturnOrderResponse>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          ResponseBackCode.ERROR_PARAM_INVALID.getMessage() + "：销售订单号不存在"
      );
    }

    if (!orders.getAgencyCode().equals(agencyCodeResponse.getAgencyCode()) ||
        !orders.getShopCode().equals(agencyCodeResponse.getShopCode())
        ) {
      return new ResponseData<ReturnOrderResponse>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          ResponseBackCode.ERROR_PARAM_INVALID.getMessage() + "：该订单不属于该门店"
      );
    }

    if (!OrderStatus.getCanReturnOrderStatus().contains(orders.getStatusId())) {
      return new ResponseData<ReturnOrderResponse>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          ResponseBackCode.ERROR_PARAM_INVALID.getMessage() + "：销售订单不在可退货退款的状态中"
      );
    }

    if (!RefundType.contains(salesReturnOrderRequest.getReturnType())) {
      return new ResponseData<ReturnOrderResponse>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          ResponseBackCode.ERROR_PARAM_INVALID.getMessage() + "：退货单退货方式值不对"
      );
    }

    if (salesReturnOrderRequest.getReturnSkuDetails().size() == 0) {
      return new ResponseData<ReturnOrderResponse>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          ResponseBackCode.ERROR_PARAM_INVALID.getMessage() + "：退货详情为空"
      );
    }

    for (ReturnSkuDetailVo detailVo : salesReturnOrderRequest.getReturnSkuDetails()) {
      if (Objects.isNull(detailVo.getQuantity()) || Objects.isNull(detailVo.getRefundedAmount())) {
        return new ResponseData<ReturnOrderResponse>(
            ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
            "退货数量与退货金蝶都不能为空，退货数量>=0, 退款金额>=0.00,订单交易号为" + detailVo.getOrderDetailNo()
        );
      }

      if (detailVo.getQuantity().equals(0) && detailVo.getRefundedAmount().compareTo(BigDecimal.ZERO) == 0) {
        return new ResponseData<ReturnOrderResponse>(
            ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
            "退货数量与退货金蝶不能同时为0，订单交易号为" + detailVo.getOrderDetailNo()
        );
      }

      if(detailVo.getQuantity().compareTo(0) < 0 || detailVo.getRefundedAmount().compareTo(BigDecimal.ZERO) < 0) {
        return new ResponseData<ReturnOrderResponse>(
            ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
            "退货数量与退货金蝶都不能小于0，退货数量>=0, 退款金额>=0.00,订单交易号为" + detailVo.getOrderDetailNo()
        );
      }


    }

    List<OrderDetail> orderDetails = orderDetailService.selectList(new EntityWrapper<>(new OrderDetail(orders.getId())));
    HashMap<String, OrderDetail> orderDetailHashMap = new HashMap<>(orderDetails.size());

    orderDetails.forEach(it -> {
      orderDetailHashMap.put(it.getDetailNo(), it);
    });

    for (ReturnSkuDetailVo detailVo : salesReturnOrderRequest.getReturnSkuDetails()) {
      OrderDetail orderDetail = orderDetailHashMap.get(detailVo.getOrderDetailNo());

      if (Objects.isNull(orderDetail) ||
          (orderDetail.getRefundQuantity().compareTo(detailVo.getQuantity()) < 0 ||
           (orderDetail.getRealAmount() !=null && orderDetail.getRealAmount().compareTo(detailVo.getRefundedAmount()) < 0)
          )) {
        return new ResponseData<ReturnOrderResponse>(
            ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
            "不符合条件：该销售订单没有该订单详情；退货数量应≤可退数量；退款金额应≤可退金额，订单交易号为" + detailVo.getOrderDetailNo()
        );
      }
    }

    ReturnOrder returnOrder = returnOrderService.create(orders, salesReturnOrderRequest, agencyCodeResponse);

    ReturnOrderResponse returnOrderResponse = new ReturnOrderResponse();

    BeanUtils.copyProperties(returnOrder, returnOrderResponse);

    return new ResponseData<ReturnOrderResponse>(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        returnOrderResponse
    );
  }

  @LogAnnotation
  @ApiOperation(nickname = "frontAuditReturnOrder", value = "前台审核销售退货单")
  @PostMapping("/audit/{orderNo}")
  public ResponseData<ReturnOrderResponse> audit(@PathVariable String orderNo) {
    AgencyCodeResponse agencyCodeResponse = salesUserService.getUserByToken();

    ReturnOrder returnOrder = returnOrderService.setAndGetOwnReturnOrder(orderNo);
    if (Objects.isNull(returnOrder)) {
      return new ResponseData<ReturnOrderResponse>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          "退货单号不存在"
      );
    }

    if (returnOrder.getStatusId().equals(AuditStatus.AUDITED.getValue())) {
      return new ResponseData<ReturnOrderResponse>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          "退货单已审核"
      );
    }

    if (!returnOrder.getAgencyCode().equals(agencyCodeResponse.getAgencyCode()) ||
        !returnOrder.getShopCode().equals(agencyCodeResponse.getShopCode())
        ) {
      return new ResponseData<ReturnOrderResponse>(
          ResponseBackCode.ERROR_AUTH_FAIL.getValue(),
          ResponseBackCode.ERROR_AUTH_FAIL.getMessage() + "：该退货单不属于该门店"
      );
    }

    returnOrder.setAuditorId(Long.valueOf(agencyCodeResponse.getUserId()));
    returnOrder.setAuditedAt(LocalDateTime.now());

    Boolean isSuccess = returnOrderService.audit(returnOrder);

    if (isSuccess) {
      pointService.resumePointByReturnOrder(returnOrder.getOrderId());
    }

    return new ResponseData<ReturnOrderResponse>(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage()
    );
  }

  /**
   * 获取销售退货单列表
   */
  @ApiOperation(nickname = "frontGetReturnOrderList", value = "获取前台销售退货单列表")
  @PostMapping(value = "/index", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData<Page<IndexReturnOrdersResponse>> index(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
      @RequestBody IndexReturnOrdersRequest request
  ) {

    // 得到经销商信息
    AgencyCodeResponse agencyInfoVo = salesUserService.getUserByToken();
    ReturnOrder returnOrder = new ReturnOrder();
    returnOrder.setAgencyCode(agencyInfoVo.getAgencyCode());
    returnOrder.setShopCode(agencyInfoVo.getShopCode());
    agencyInfoVo = null;

    Page<IndexReturnOrdersResponse> orderVoPage = new Page<>(page, size);
    orderVoPage.setAsc(false);
    orderVoPage = returnOrderService.selectReturnOrderPage(returnOrder, orderVoPage, request,false);
    return new ResponseData<Page<IndexReturnOrdersResponse>>(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        orderVoPage
    );
  }

  @ApiOperation(nickname = "frontShowReturnOrder", value = "前台获取退货单详情")
  @GetMapping("/{orderNo}")
  public ResponseData<IndexReturnOrdersResponse> show(@PathVariable String orderNo) {
    ReturnOrder returnOrder = returnOrderService.setAndGetOwnReturnOrder(orderNo);
    if (Objects.isNull(returnOrder)) {
      return new ResponseData<IndexReturnOrdersResponse>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          "退货单号不存在"
      );
    }

    returnOrder = returnOrderService.selectOne(new EntityWrapper<>(returnOrder));
    if (Objects.isNull(returnOrder)) {
      return new ResponseData<IndexReturnOrdersResponse>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          "退货单号不存在"
      );
    }
    IndexReturnOrdersResponse response = new IndexReturnOrdersResponse();
    BeanUtils.copyProperties(returnOrder, response);

    response.setAgencyName(erpConfig.getAllAgencyAndShopNameAndSourceMap().get(returnOrder.getAgencyCode()));
    response.setShopName(erpConfig.getAllAgencyAndShopNameAndSourceMap().get(returnOrder.getShopCode()));

    List<ReturnOrderDetailVo> returnOrderDetailVos = null;
    try {
      returnOrderDetailVos = returnOrderDetailService.selectReturnOrderDetail(returnOrder.getId());
    } catch (Exception e) {
      e.printStackTrace();
    }

    // 设置销售订单号
    Orders orders = ordersService.selectById(returnOrder.getOrderId());
    response.setSalesOrderNo(orders.getNo());

    orders = null;
    returnOrder = null;

    response.setReturnOrderDetailVos(returnOrderDetailVos);

    return new ResponseData<IndexReturnOrdersResponse>(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        response
    );
  }

  /**
   * 取消订单
   */
  @LogAnnotation
  @PutMapping(value = "/cancel/{orderNo}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData cancelReturnOrder(@PathVariable String orderNo) {

    ReturnOrder returnOrder = returnOrderService.setAndGetOwnReturnOrder(orderNo);
    if (Objects.isNull(returnOrder)) {
      return new ResponseData<IndexReturnOrdersResponse>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          "退货单号不存在"
      );
    }

    if (returnOrder.getStatusId().equals(AuditStatus.AUDITED.getValue())) {
      return new ResponseData<ReturnOrderResponse>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          "退货单号已审核 不能取消"
      );
    }
    if (returnOrder.getStatusId().equals(AuditStatus.CANCEL.getValue())) {
      return new ResponseData<ReturnOrderResponse>(
          ResponseBackCode.SUCCESS.getValue(),
          ResponseBackCode.SUCCESS.getMessage()
      );
    }

    returnOrder.setStatusId(AuditStatus.CANCEL.getValue());
    returnOrderService.updateById(returnOrder);
    return new ResponseData<ReturnOrderResponse>(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage()
    );
  }
}
