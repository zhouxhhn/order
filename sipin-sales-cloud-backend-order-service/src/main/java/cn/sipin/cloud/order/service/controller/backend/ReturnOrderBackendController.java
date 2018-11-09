/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.controller.backend;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.sipin.cloud.order.service.config.ErpConfig;
import cn.sipin.cloud.order.service.feign.impl.SalesUserServiceImpl;
import cn.sipin.cloud.order.service.service.OrdersServiceContract;
import cn.sipin.cloud.order.service.service.PaymentServiceContract;
import cn.sipin.cloud.order.service.service.ReturnOrderDetailServiceContract;
import cn.sipin.cloud.order.service.service.ReturnOrderServiceContract;
import cn.sipin.sales.cloud.order.constants.OrderStatus;
import cn.sipin.sales.cloud.order.constants.PaymentConstants;
import cn.sipin.sales.cloud.order.pojo.Orders;
import cn.sipin.sales.cloud.order.pojo.Payment;
import cn.sipin.sales.cloud.order.pojo.ReturnOrder;
import cn.sipin.sales.cloud.order.request.returnOrder.IndexReturnOrdersRequest;
import cn.sipin.sales.cloud.order.response.AgencyCodeResponse;
import cn.sipin.sales.cloud.order.response.PaymentResponse;
import cn.sipin.sales.cloud.order.response.returnOrder.IndexReturnOrdersResponse;
import cn.sipin.sales.cloud.order.response.vo.ReturnOrderDetailVo;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "后台销售退货单")
@RequestMapping(path = "/backend/return-order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ReturnOrderBackendController {

  private ReturnOrderServiceContract returnOrderService;

  private ReturnOrderDetailServiceContract returnOrderDetailService;

  private SalesUserServiceImpl salesUserService;

  private ErpConfig erpConfig;

  private OrdersServiceContract ordersService;

  private PaymentServiceContract paymentService;

  @Autowired
  public ReturnOrderBackendController(
      ReturnOrderServiceContract returnOrderService, ReturnOrderDetailServiceContract returnOrderDetailService,
      SalesUserServiceImpl salesUserService,
      ErpConfig erpConfig,
      OrdersServiceContract ordersService,
      PaymentServiceContract paymentService
  ) {
    this.returnOrderService = returnOrderService;
    this.returnOrderDetailService = returnOrderDetailService;
    this.salesUserService = salesUserService;
    this.erpConfig = erpConfig;
    this.ordersService = ordersService;
    this.paymentService = paymentService;
  }

  /**
   * 获取销售退货单列表
   */
  @ApiOperation(nickname = "backendGetReturnOrderList", value = "获取后台销售退货单列表")
  @PostMapping(value = "/index", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData<Page<IndexReturnOrdersResponse>> index(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
      @RequestBody IndexReturnOrdersRequest request
  ) {
    // 得到经销商信息
    AgencyCodeResponse agencyInfoVo = salesUserService.getUserByToken();

    Page<IndexReturnOrdersResponse> orderVoPage = new Page<>(page, size);
    if (StringUtils.isBlank(agencyInfoVo.getShopCode())) {
      // 如果门店为空或者空字符串，则是斯品内部管理员，可查询所有退货订单
      orderVoPage = returnOrderService.selectReturnOrderPage(null, orderVoPage, request,false);
      return new ResponseData<Page<IndexReturnOrdersResponse>>(
          ResponseBackCode.SUCCESS.getValue(),
          ResponseBackCode.SUCCESS.getMessage(),
          orderVoPage
      );
    }

    ReturnOrder returnOrder = new ReturnOrder();
    returnOrder.setAgencyCode(agencyInfoVo.getAgencyCode());
    returnOrder.setShopCode(agencyInfoVo.getShopCode());

    orderVoPage.setAsc(false);
    orderVoPage = returnOrderService.selectReturnOrderPage(returnOrder, orderVoPage, request,false);
    return new ResponseData<Page<IndexReturnOrdersResponse>>(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        orderVoPage
    );
  }

  @ApiOperation(nickname = "backendShowReturnOrder", value = "后台台获取退货单详情")
  @GetMapping("/{orderNo}")
  public ResponseData<IndexReturnOrdersResponse> show(@PathVariable String orderNo) {
    if (StringUtils.isBlank(orderNo)) {
      return new ResponseData<IndexReturnOrdersResponse>(
          ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
          "退货单号不存在"
      );
    }

    // 得到经销商信息
    AgencyCodeResponse agencyInfoVo = salesUserService.getUserByToken();

    ReturnOrder returnOrder = new ReturnOrder();
    returnOrder.setNo(orderNo);

    if (StringUtils.isNotBlank(agencyInfoVo.getShopCode())) {
      // 如果门店为空或者空字符串，则是斯品内部管理员，可查询所有退货退款订单详情
      // 否则 为经销商管理员可查 只能查自己门店退货单
      returnOrder.setAgencyCode(agencyInfoVo.getAgencyCode());
      returnOrder.setShopCode(agencyInfoVo.getShopCode());
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

    Orders orders =  ordersService.selectById(returnOrder.getOrderId());
    response.setSalesOrderNo(orders.getNo());

    List<ReturnOrderDetailVo> returnOrderDetailVos = returnOrderDetailService.selectReturnOrderDetail(returnOrder.getId());

    returnOrder = null;

    response.setReturnOrderDetailVos(returnOrderDetailVos);

    //设置支付列表数据

    List<Payment> paymentList =  paymentService.selectList(new EntityWrapper<>(new Payment(orders.getId())));
    List<PaymentResponse> responseList = new ArrayList<>();
    if(paymentList != null && !paymentList.isEmpty()){
      for (int i = 0,size = paymentList.size(); i < size; i++) {
        PaymentResponse paymentResponse = new PaymentResponse();
        Payment payment = paymentList.get(i);
        BeanUtils.copyProperties(payment,paymentResponse);
        paymentResponse.setPrice(payment.getRealReceivePrice());
        paymentResponse.setPaymentMethod(PaymentConstants.getDescriptionByValue(payment.getPartnerId()));
        responseList.add(paymentResponse);
      }
    }
    response.setPaymentList(responseList);

    return new ResponseData<IndexReturnOrdersResponse>(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        response
    );
  }

  /**
   * 导出销售退货单列表
   */
  @PostMapping(value = "/exportExcel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData exportExcel( @RequestBody IndexReturnOrdersRequest request) {
    // 得到经销商信息
    AgencyCodeResponse agencyInfoVo = salesUserService.getUserByToken();

    Page<IndexReturnOrdersResponse> orderVoPage = new Page<>();

    if (StringUtils.isBlank(agencyInfoVo.getShopCode())) {
      // 如果门店为空或者空字符串，则是斯品内部管理员，可查询所有退货订单
      orderVoPage = returnOrderService.selectReturnOrderPage(null, orderVoPage, request,true);
      return  ResponseData.build( ResponseBackCode.SUCCESS.getValue(), ResponseBackCode.SUCCESS.getMessage(),orderVoPage.getRecords());
    }

    ReturnOrder returnOrder = new ReturnOrder();
    returnOrder.setAgencyCode(agencyInfoVo.getAgencyCode());
    returnOrder.setShopCode(agencyInfoVo.getShopCode());

    orderVoPage.setAsc(false);
    orderVoPage = returnOrderService.selectReturnOrderPage(returnOrder, orderVoPage, request,true);
    return  ResponseData.build( ResponseBackCode.SUCCESS.getValue(), ResponseBackCode.SUCCESS.getMessage(), orderVoPage.getRecords());
  }

}
