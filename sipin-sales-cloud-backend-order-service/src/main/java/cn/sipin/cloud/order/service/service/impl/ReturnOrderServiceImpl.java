package cn.sipin.cloud.order.service.service.impl;

import cn.sipin.cloud.order.service.mapper.param.ReturnOrderSearchParam;
import cn.sipin.cloud.order.service.service.PaymentServiceContract;
import cn.sipin.sales.cloud.order.common.PageResponse;
import cn.sipin.sales.cloud.order.dto.returnorder.ReturnOrderDto;
import cn.sipin.sales.cloud.order.dto.returnorderdetail.ReturnOrderDetailDto;
import cn.sipin.sales.cloud.order.request.returnOrder.ReturnOrderSearchRequest;
import cn.sipin.sales.cloud.order.response.returnOrder.ReturnOrderDetailSearchVo;
import cn.sipin.sales.cloud.order.response.returnOrder.ReturnOrderVo;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.util.ResponseUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.stream.Collectors;

import cn.sipin.cloud.order.service.config.ErpConfig;
import cn.sipin.cloud.order.service.feign.impl.SalesUserServiceImpl;
import cn.sipin.cloud.order.service.mapper.ReturnOrderMapper;
import cn.sipin.cloud.order.service.service.OrderDetailServiceContract;
import cn.sipin.cloud.order.service.service.OrdersServiceContract;
import cn.sipin.cloud.order.service.service.ReturnOrderDetailServiceContract;
import cn.sipin.cloud.order.service.service.ReturnOrderServiceContract;
import cn.sipin.cloud.order.service.util.GenerateDistributedID;
import cn.sipin.sales.cloud.order.constants.AuditStatus;
import cn.sipin.sales.cloud.order.pojo.OrderDetail;
import cn.sipin.sales.cloud.order.pojo.Orders;
import cn.sipin.sales.cloud.order.pojo.ReturnOrder;
import cn.sipin.sales.cloud.order.pojo.ReturnOrderDetail;
import cn.sipin.sales.cloud.order.request.returnOrder.IndexReturnOrdersRequest;
import cn.sipin.sales.cloud.order.request.returnOrder.SalesReturnOrderRequest;
import cn.sipin.sales.cloud.order.request.vo.ReturnSkuDetailVo;
import cn.sipin.sales.cloud.order.response.AgencyCodeResponse;
import cn.sipin.sales.cloud.order.response.returnOrder.IndexReturnOrdersResponse;
import cn.sipin.sales.cloud.order.response.vo.ReturnOrderDetailVo;
import cn.siyue.platform.constants.ResponseBackCode;
import cn.siyue.platform.exceptions.exception.RequestException;

/**
 * <p>
 * 销售退货单 服务实现类
 * </p>
 */
@Service
public class ReturnOrderServiceImpl extends ServiceImpl<ReturnOrderMapper, ReturnOrder> implements ReturnOrderServiceContract {

  //TODO 退货单表需要有一个核销积分字段，重复核销

  private OrdersServiceContract ordersService;

  private OrderDetailServiceContract orderDetailService;

  private ReturnOrderDetailServiceContract returnOrderDetailService;

  private SalesUserServiceImpl salesUserService;

  private ErpConfig erpConfig;

  private ReturnOrderMapper returnOrderMapper;

  private PaymentServiceContract paymentService;

  @Autowired
  public ReturnOrderServiceImpl(
      OrdersServiceContract ordersService, OrderDetailServiceContract orderDetailService,
      ReturnOrderDetailServiceContract returnOrderDetailService,
      SalesUserServiceImpl salesUserService,
      ErpConfig erpConfig,
      ReturnOrderMapper returnOrderMapper,
      PaymentServiceContract paymentService
  ) {
    this.ordersService = ordersService;
    this.orderDetailService = orderDetailService;
    this.returnOrderDetailService = returnOrderDetailService;
    this.salesUserService = salesUserService;
    this.erpConfig = erpConfig;
    this.returnOrderMapper = returnOrderMapper;
    this.paymentService = paymentService;
  }

  @Override public ReturnOrder setAndGetOwnReturnOrder(String returnOrderNo) {
    if (StringUtils.isBlank(returnOrderNo)) {
      return null;
    }

    // 得到经销商信息
    AgencyCodeResponse agencyInfoVo = salesUserService.getUserByToken();
    ReturnOrder returnOrder = new ReturnOrder();
    returnOrder.setAgencyCode(agencyInfoVo.getAgencyCode());
    returnOrder.setShopCode(agencyInfoVo.getShopCode());
    returnOrder.setNo(returnOrderNo);
    returnOrder = this.selectOne(new EntityWrapper<>(returnOrder));
    agencyInfoVo = null;

    return returnOrder;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override public ReturnOrder create(Orders orders, SalesReturnOrderRequest salesReturnOrderRequest, AgencyCodeResponse agencyCodeResponse) {

    ReturnOrder returnOrder = new ReturnOrder();
    returnOrder.setOrderId(orders.getId());
    returnOrder.setAgencyCode(orders.getAgencyCode());
    returnOrder.setShopCode(orders.getShopCode());
    returnOrder.setRefundType(salesReturnOrderRequest.getReturnType());
    returnOrder.setReasonNote(salesReturnOrderRequest.getReasonNote());
    returnOrder.setMobile(orders.getMobile());
    returnOrder.setCreatorId(Long.valueOf(agencyCodeResponse.getUserId()));
    returnOrder.setStatusId(AuditStatus.PENDING.getValue());
    returnOrder.setNo(GenerateDistributedID.getReturnOrderNo(orders.getAgencyCode()));

    this.insert(returnOrder);

    List<OrderDetail> orderDetails = orderDetailService.selectList(new EntityWrapper<>(new OrderDetail(orders.getId())));
    HashMap<String, OrderDetail> detailHashMap = new HashMap<>(orderDetails.size());
    orderDetails.forEach(it -> {
      detailHashMap.put(it.getDetailNo(), it);
    });

    List<ReturnOrderDetail> returnOrderDetails = new ArrayList<>(salesReturnOrderRequest.getReturnSkuDetails().size());

    int count = 1;

    BigDecimal allRefundedAmount = BigDecimal.ZERO;

    for (ReturnSkuDetailVo detailVo : salesReturnOrderRequest.getReturnSkuDetails()) {
      ReturnOrderDetail returnOrderDetail = new ReturnOrderDetail();
      OrderDetail orderDetail = detailHashMap.get(detailVo.getOrderDetailNo());

      returnOrderDetail.setDetailNo(returnOrder.getNo() + String.format("%03d", count++));
      returnOrderDetail.setOrderDetailId(orderDetail.getId());
      returnOrderDetail.setReturnOrderId(returnOrder.getId());
      returnOrderDetail.setSkuNo(orderDetail.getSkuNo());
      if (detailVo.getRefundedAmount() != null) {
        returnOrderDetail.setRefundedAmount(detailVo.getRefundedAmount());
        allRefundedAmount = allRefundedAmount.add(detailVo.getRefundedAmount());
      }

      if (detailVo.getQuantity() != null) {
        returnOrderDetail.setRefundedQuantity(detailVo.getQuantity());
      }

      returnOrderDetails.add(returnOrderDetail);
    }

    // 更新退款总金额
    returnOrder.setRefundedAmount(allRefundedAmount.setScale(2));
    this.baseMapper.updateById(returnOrder);

    returnOrderDetailService.insertBatch(returnOrderDetails);

    return returnOrder;
  }

  @Override public ReturnOrder selectByNo(String no) {
    if (StringUtils.isBlank(no)) {
      return null;
    }
    return this.baseMapper.selectByNo(no);
  }

  @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
  @Override public Boolean audit(ReturnOrder returnOrder) {

    List<OrderDetail> orderDetails = orderDetailService.selectList(new EntityWrapper<>(new OrderDetail(returnOrder.getOrderId())));
    HashMap<Long, OrderDetail> detailHashMap = new HashMap<>(orderDetails.size());
    orderDetails.forEach(it -> {
      detailHashMap.put(it.getId(), it);
    });
    List<ReturnOrderDetail> returnOrderDetails = returnOrderDetailService.selectList(new EntityWrapper<>(new ReturnOrderDetail(returnOrder.getId())));

    BigDecimal refundedAmount = BigDecimal.ZERO;

    for (ReturnOrderDetail detail : returnOrderDetails) {
      OrderDetail orderDetail = detailHashMap.get(detail.getOrderDetailId());
      if (Objects.isNull(orderDetail)) {

        throw new RequestException(ResponseBackCode.ERROR_NOT_FOUND.getValue(), "退货单详情无法对应订单详情");
      }

      if (detail.getRefundedQuantity().compareTo(orderDetail.getRefundQuantity()) > 0) {
        throw new RequestException(ResponseBackCode.ERROR_PARAM_INVALID.getValue(), "退货单详情可退数量不对");
      }

      if (detail.getRefundedAmount().compareTo(orderDetail.getRefundAmount()) > 0) {
        throw new RequestException(ResponseBackCode.ERROR_PARAM_INVALID.getValue(), "退货单详情可退金额不对");
      }
    }

    Orders orders = ordersService.selectById(returnOrder.getOrderId());
    BigDecimal orderRefundAmount = orders.getRefundAmount().subtract(refundedAmount, MathContext.DECIMAL128);

    if (orderRefundAmount.compareTo(BigDecimal.ZERO) < 0) {
      throw new RequestException(ResponseBackCode.ERROR_PARAM_INVALID.getValue(), "退货单可退金额不对");
    }

    ReturnOrder tempReturnOrder = new ReturnOrder();
    tempReturnOrder.setId(returnOrder.getId());
    tempReturnOrder.setStatusId(AuditStatus.AUDITED.getValue());
    tempReturnOrder.setAuditorId(returnOrder.getAuditorId());
    tempReturnOrder.setAuditedAt(returnOrder.getAuditedAt());
    this.updateById(tempReturnOrder);

    Orders tempOrder = new Orders();
    tempOrder.setId(returnOrder.getOrderId());
    tempOrder.setRefundAmount(orderRefundAmount);

    Boolean isSuccess = ordersService.updateById(tempOrder);

    for (ReturnOrderDetail detail : returnOrderDetails) {
      OrderDetail orderDetail = detailHashMap.get(detail.getOrderDetailId());
      BigDecimal orderDetailRefundAmount = orderDetail.getRefundAmount()
          .subtract(detail.getRefundedAmount())
          .setScale(2);

      Integer orderDetailRefundQuantity = orderDetail.getRefundQuantity() - detail.getRefundedQuantity();
      orderDetail.setRefundAmount(orderDetailRefundAmount);
      orderDetail.setRefundQuantity(orderDetailRefundQuantity);
      orderDetailService.updateById(orderDetail);
    }

    paymentService.returnPayment(String.valueOf(returnOrder.getRefundType()), returnOrder.getRefundedAmount(), returnOrder.getOrderId());

    //TODO 这里可能要先做退货退积分流程
    return true;
  }

  @Override public Page<IndexReturnOrdersResponse> selectReturnOrderPage(
      ReturnOrder returnOrder,
      Page<IndexReturnOrdersResponse> orderVoPage,
      IndexReturnOrdersRequest request,boolean flag
  ) {

    List<ReturnOrder> returnOrders = null;
    try {
      //是出是导出excel，导出excel时，要全部导出
      if(flag){
        returnOrders = baseMapper.selectReturnOrderPage(returnOrder, null, null, request);
      }else{
        returnOrders = baseMapper.selectReturnOrderPage(returnOrder, orderVoPage.getLimit(), orderVoPage.getOffset(), request);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    if (returnOrders.size() > 0) {

      Map<Long,String> userMap =  salesUserService.getUserInfoList();

      // 获取订单列表的订单详情列表--开始
      List<Long> orderIds = returnOrders.stream().map(it -> it.getOrderId()).collect(Collectors.toList());
      List<OrderDetail> orderDetails = orderDetailService.selectListByOrderIds(orderIds);
      // <商品交易号,订单详情>hashmap用于提取订单商品属性给targetDetailVo
      HashMap<Long, OrderDetail> orderDetailHashMap = new HashMap<>(orderDetails.size());
      for(OrderDetail orderDetail : orderDetails) {
        orderDetailHashMap.put(orderDetail.getId(), orderDetail);
      }
      // 获取订单列表的订单详情列表--结束

      List<IndexReturnOrdersResponse> returnOrdersResponses = new ArrayList<>(returnOrders.size());
      returnOrders.forEach(it -> {
        IndexReturnOrdersResponse response = new IndexReturnOrdersResponse();
        BeanUtils.copyProperties(it, response);

        response.setAgencyName(erpConfig.getAllAgencyAndShopNameAndSourceMap().get(it.getAgencyCode()));
        response.setShopName(erpConfig.getAllAgencyAndShopNameAndSourceMap().get(it.getShopCode()));
        if(userMap != null && it.getCreatorId() != null){
          response.setCreatorName(userMap.get(it.getCreatorId()));
        }

        List<ReturnOrderDetail> souceDetails = returnOrderDetailService.selectList(
            new EntityWrapper<>(
                new ReturnOrderDetail(it.getId())
            ));
        List<ReturnOrderDetailVo> targetDetailVos = new ArrayList<>(souceDetails.size());
        souceDetails.forEach(souceDetail -> {
          ReturnOrderDetailVo targetDetailVo = new ReturnOrderDetailVo();
          BeanUtils.copyProperties(souceDetail, targetDetailVo);

          OrderDetail orderDetail = orderDetailHashMap.get(souceDetail.getOrderDetailId());

          targetDetailVo.setName(orderDetail.getName());
          targetDetailVo.setSalesPrice(orderDetail.getRealSingleAmount());
          targetDetailVo.setColor(orderDetail.getColor());
          targetDetailVo.setSpecification(orderDetail.getSpecification());
          targetDetailVo.setTexture(orderDetail.getTexture());
          targetDetailVo.setImgPath(orderDetail.getImgPath());

          targetDetailVos.add(targetDetailVo);
        });

        response.setReturnOrderDetailVos(targetDetailVos);

        returnOrdersResponses.add(response);
      });

      orderVoPage.setRecords(returnOrdersResponses);
      Integer total = baseMapper.selectReturnOrderPageCount(returnOrder, request);

      orderVoPage.setTotal(total);
    }

    return orderVoPage;
  }

  @Override
  public ResponseData<PageResponse<ReturnOrderVo>> search(ReturnOrderSearchRequest requestParam) {
    Pagination page = new Pagination(requestParam.getPage(), requestParam.getSize());
    ReturnOrderSearchParam searchParam = new ReturnOrderSearchParam();
    BeanUtils.copyProperties(requestParam, searchParam);
    List<ReturnOrderDto> list = returnOrderMapper.search(searchParam, page);
    PageResponse<ReturnOrderVo> pageResponse = null;
    if (list != null && list.size() > 0) {
      Map<Long, ReturnOrderVo> returnOrderMap = new HashMap<Long, ReturnOrderVo>();
      List<ReturnOrderVo> respList = new ArrayList<ReturnOrderVo>();

      List<Long> returnOrderIdList = new ArrayList<Long>();

      HashMap<String, String> outerCodeMap = erpConfig.getAllAgencyMap();
      HashMap<String, String> sourceIdMap = erpConfig.getAllAgencyAndShopNameAndSourceMap();
      for (ReturnOrderDto roDto : list) {
        ReturnOrderVo returnOrderVo = new ReturnOrderVo();
        BeanUtils.copyProperties(roDto, returnOrderVo);

        // 外部编码
        String outerCode = outerCodeMap.get(roDto.getAgencyCode());
        returnOrderVo.setOuterCode(outerCode);
        String sourceKey = roDto.getAgencyCode() + roDto.getShopCode();
        String sourceId = sourceIdMap.get(sourceKey);
        returnOrderVo.setSourceId(sourceId);

        respList.add(returnOrderVo);
        returnOrderIdList.add(roDto.getId());
        returnOrderMap.put(roDto.getId(), returnOrderVo);
      }

      List<ReturnOrderDetailDto> orderDetailList = returnOrderDetailService.getListByReturnOrderIds(returnOrderIdList);
      if (orderDetailList != null && !orderDetailList.isEmpty()) {
        List<ReturnOrderDetailSearchVo> detailList = null;
        Map<Long, List<ReturnOrderDetailSearchVo>> orderListMap = new HashMap<Long, List<ReturnOrderDetailSearchVo>>();
        for (ReturnOrderDetailDto od : orderDetailList) {
          detailList = orderListMap.get(od.getReturnOrderId());
          if (detailList == null) {
            detailList = new ArrayList<ReturnOrderDetailSearchVo>();
            ReturnOrderVo returnOrderVo = returnOrderMap.get(od.getReturnOrderId());
            returnOrderVo.setDetailList(detailList);
            orderListMap.put(od.getReturnOrderId(), detailList);
          }

          ReturnOrderDetailSearchVo odVo = new ReturnOrderDetailSearchVo();
          BeanUtils.copyProperties(od, odVo);
          detailList.add(odVo);
        }
      }

      pageResponse = new PageResponse<ReturnOrderVo>();
      pageResponse.setRecords(respList);
      pageResponse.setPages(page.getPages());
      pageResponse.setTotal(page.getTotal());
      pageResponse.setCurrent(page.getCurrent());
      pageResponse.setSize(page.getSize());
    }
    return ResponseUtil.success(pageResponse);
  }


}
