package cn.sipin.cloud.order.service.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.sipin.cloud.order.service.config.ErpConfig;
import cn.sipin.cloud.order.service.contract.Loggable;
import cn.sipin.cloud.order.service.feign.impl.SalesUserServiceImpl;
import cn.sipin.cloud.order.service.feign.service.MaterialService;
import cn.sipin.cloud.order.service.feign.service.SalesUserService;
import cn.sipin.cloud.order.service.mapper.OrdersMapper;
import cn.sipin.cloud.order.service.service.OrderConsigneeServiceContract;
import cn.sipin.cloud.order.service.service.OrderDetailServiceContract;
import cn.sipin.cloud.order.service.service.OrderDiscountServiceContract;
import cn.sipin.cloud.order.service.service.OrderExpressServiceContract;
import cn.sipin.cloud.order.service.service.OrdersServiceContract;
import cn.sipin.cloud.order.service.service.PaymentServiceContract;
import cn.sipin.cloud.order.service.service.SipinServiceContract;
import cn.sipin.cloud.order.service.sipin.constant.CouponDiscountTypeEnum;
import cn.sipin.cloud.order.service.sipin.constant.PointType;
import cn.sipin.cloud.order.service.sipin.vo.member.MemberApi;
import cn.sipin.cloud.order.service.sipin.vo.member.MemberApiCoupon;
import cn.sipin.cloud.order.service.util.GenerateDistributedID;
import cn.sipin.cloud.order.service.util.JsonTransformer;
import cn.sipin.sales.cloud.order.constants.OrderConstants;
import cn.sipin.sales.cloud.order.constants.OrderDiscountTypeId;
import cn.sipin.sales.cloud.order.constants.OrderStatus;
import cn.sipin.sales.cloud.order.constants.PaymentConstants;
import cn.sipin.sales.cloud.order.pojo.OrderConsignee;
import cn.sipin.sales.cloud.order.pojo.OrderDetail;
import cn.sipin.sales.cloud.order.pojo.OrderDiscount;
import cn.sipin.sales.cloud.order.pojo.OrderExpress;
import cn.sipin.sales.cloud.order.pojo.Orders;
import cn.sipin.sales.cloud.order.pojo.Payment;
import cn.sipin.sales.cloud.order.request.SalesOrderRequest;
import cn.sipin.sales.cloud.order.request.backend.index.IndexOrdersRequest;
import cn.sipin.sales.cloud.order.request.confirmPayment.ConfirmPaymentRequest;
import cn.sipin.sales.cloud.order.request.editNotes.EditNotesRequest;
import cn.sipin.sales.cloud.order.request.erp.ErpOrdersRequest;
import cn.sipin.sales.cloud.order.request.wholeDiscount.WholeDiscountRequest;
import cn.sipin.sales.cloud.order.response.AgencyCodeResponse;
import cn.sipin.sales.cloud.order.response.ErpSalesOrderResponse;
import cn.sipin.sales.cloud.order.response.MaterialResponse;
import cn.sipin.sales.cloud.order.response.OrderConsigneeResponse;
import cn.sipin.sales.cloud.order.response.PaymentResponse;
import cn.sipin.sales.cloud.order.response.SalesOrderDetailResponse;
import cn.sipin.sales.cloud.order.response.backend.detail.DetailOrdersResponse;
import cn.sipin.sales.cloud.order.response.backend.detail.LogisticResponse;
import cn.sipin.sales.cloud.order.response.backend.export.ExportIndexOrdersResponse;
import cn.sipin.sales.cloud.order.response.backend.index.IndexOrdersResponse;
import cn.sipin.sales.cloud.order.response.vo.OrderConsigneeVo;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

/**
 * <p>
 * 销售订单 服务实现类
 * </p>
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersServiceContract, Loggable {

  public final static String ANONYMOUS_USER = "Anonymous-User";

  private OrderDetailServiceContract orderDetailService;

  private OrderConsigneeServiceContract orderConsigneeService;

  private OrderDiscountServiceContract orderDiscountService;

  private SalesUserService salesUserService;

  private SalesUserServiceImpl salesUserServiceImpl;

  private PaymentServiceContract paymentService;

  private SipinServiceContract sipinService;

  private OrderExpressServiceContract orderExpressService;

  private ErpConfig erpConfig;

  private MaterialService materialService;

  @Autowired
  public OrdersServiceImpl(
      OrderDetailServiceContract orderDetailService, OrderConsigneeServiceContract orderConsigneeService,
      OrderDiscountServiceContract orderDiscountService,
      SalesUserService salesUserService,
      SalesUserServiceImpl salesUserServiceImpl,
      PaymentServiceContract paymentService,
      SipinServiceContract sipinService,
      OrderExpressServiceContract orderExpressService,
      ErpConfig erpConfig,
      MaterialService materialService
  ) {
    this.orderDetailService = orderDetailService;
    this.orderConsigneeService = orderConsigneeService;
    this.orderDiscountService = orderDiscountService;
    this.salesUserService = salesUserService;
    this.salesUserServiceImpl = salesUserServiceImpl;
    this.paymentService = paymentService;
    this.sipinService = sipinService;
    this.orderExpressService = orderExpressService;
    this.erpConfig = erpConfig;
    this.materialService = materialService;
  }

  @Override public Orders setAndGetOwnOrders(String orderNo) {
    if (StringUtils.isBlank(orderNo)) {
      return null;
    }

    // 得到经销商信息
    AgencyCodeResponse agencyInfoVo = salesUserServiceImpl.getUserByToken();
    Orders orders = new Orders();
    orders.setAgencyCode(agencyInfoVo.getAgencyCode());
    orders.setShopCode(agencyInfoVo.getShopCode());
    orders.setNo(orderNo);
    orders = this.selectOne(new EntityWrapper<>(orders));
    agencyInfoVo = null;

    return orders;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override public Orders create(
      SalesOrderRequest salesOrderRequest, AgencyCodeResponse agencyInfoVo, MemberApi member,
      List<OrderDetail> orderDetailList,
      MemberApiCoupon coupon
  ) {
    Orders orders = new Orders();
    List<OrderDiscount> orderDiscounts = new ArrayList<>();

    orders.setNo(GenerateDistributedID.getSalesOrderNo(agencyInfoVo.getAgencyCode()));
    orders.setStatusId(OrderStatus.WAIT_PAY.getCode());
    orders.setAgencyCode(agencyInfoVo.getAgencyCode());
    orders.setShopCode(agencyInfoVo.getShopCode());
    // 导购员ID
    orders.setSalerId(salesOrderRequest.getSallerId());
    // 创建者ID
    orders.setCreaterId(Long.valueOf(agencyInfoVo.getUserId()));
    orders.setNote(salesOrderRequest.getNote());
    orders.setUsedPoint(0);
    if (StringUtils.isNotBlank(salesOrderRequest.getMobile())) {

      orders.setMobile(salesOrderRequest.getMobile());
      orders.setDiscount(new BigDecimal("1.0"));
      // 目前销售订单不用考虑会员折扣
      // orders.setDiscount(member.getLevel().getDiscountRate());
    } else {
      // 匿名用户
      orders.setMobile(ANONYMOUS_USER);
      orders.setDiscount(new BigDecimal("1.0"));
    }

    int count = 1;
    Boolean isAllPickup = true;
    // 订单金额
    BigDecimal orderAmount = BigDecimal.ZERO;
    for (OrderDetail detail : orderDetailList) {
      detail.setDetailNo(orders.getNo() + String.format("%03d", count++));
      // 折后价
      detail.setDiscountAmount(detail.getAmount().multiply(orders.getDiscount()));
      // 小计 = 折后价 * 数量
      detail.setSubtotal(detail.getDiscountAmount().multiply(new BigDecimal(detail.getQuantity().toString())));

      // 订单金额
      orderAmount = orderAmount.add(detail.getSubtotal());

      isAllPickup = isAllPickup & detail.getPickup();
    }

    // 是否是全自提
    orders.setPickup(isAllPickup);
    // 订单金额
    orders.setAmount(orderAmount);
    orders.setOriginalPayableAmount(orderAmount);

    // 优惠券使用
    if (!Objects.isNull(coupon)) {
      if (CouponDiscountTypeEnum.MONEY.equals(coupon.getTask().getDiscountType())) {
        // 优惠券 满减
        if (orders.getOriginalPayableAmount().compareTo(coupon.getTask().getValue()) < 0) {
          // 优惠券金额比原应付订单金额大，应付金额设置为0
          orders.setOriginalPayableAmount(BigDecimal.ZERO);
        } else {
          orders.setOriginalPayableAmount(orders.getOriginalPayableAmount()
                                              .subtract(coupon.getTask().getValue(), MathContext.DECIMAL128)
                                              .setScale(2, BigDecimal.ROUND_HALF_UP));
        }
      } else {
        // 优惠券 折扣
        orders.setOriginalPayableAmount(orders.getOriginalPayableAmount()
                                            .multiply(coupon.getTask().getValue(), MathContext.DECIMAL128)
                                            .setScale(2, BigDecimal.ROUND_HALF_UP)
        );
      }

      // 创建优惠折扣表 包括积分、满减、满折、抹零
      OrderDiscount discount = new OrderDiscount();
      discount.setCode(coupon.getCode());

      if (CouponDiscountTypeEnum.MONEY.equals(coupon.getTask().getDiscountType())) {
        discount.setTypeId(OrderDiscountTypeId.COUPON_MONEY.getValue());
      } else {
        discount.setTypeId(OrderDiscountTypeId.COUPON_DISCOUNT.getValue());
      }

      discount.setDiscountValue(coupon.getTask().getValue());

      orderDiscounts.add(discount);

      //核销优惠券
      sipinService.cancelCoupon(coupon.getCode());
    }

    // 积分使用
    if (!Objects.isNull(salesOrderRequest.getPoint()) && salesOrderRequest.getPoint() > 0) {
      // 1000 积分 可兑换10元, 100积分 -> 1元  10积分 -> 0.1元， 1积分 -> 0.01元
      BigDecimal pointtoMoney = (new BigDecimal(salesOrderRequest.getPoint().toString()))
          .divide(new BigDecimal("100"), MathContext.DECIMAL128)
          .setScale(2, BigDecimal.ROUND_HALF_UP);
      if (orders.getOriginalPayableAmount().compareTo(pointtoMoney) >= 0) {
        orders.setOriginalPayableAmount(
            orders.getOriginalPayableAmount()
                .subtract(pointtoMoney, MathContext.DECIMAL128)
                .setScale(2, BigDecimal.ROUND_HALF_UP));

        orders.setUsedPoint(salesOrderRequest.getPoint());

        OrderDiscount discount = new OrderDiscount();
        discount.setTypeId(OrderDiscountTypeId.PONIT_MONEY.getValue());
        discount.setDiscountValue(pointtoMoney);
        orderDiscounts.add(discount);

        // 核销积分 需要传负数
        int minusPoint = Math.abs(salesOrderRequest.getPoint()) * (-1);

        //核销积分
        sipinService.updatePoint(orders.getMobile(), minusPoint, PointType.DECREASE.getValue(), orders.getNo());
      }
    }

    orders.setPayableAmount(orders.getOriginalPayableAmount());
    this.baseMapper.insert(orders);
    orderDetailList.forEach(it -> it.setOrderId(orders.getId()));
    orderDetailService.insertBatch(orderDetailList);

    if (!Objects.isNull(salesOrderRequest.getAddress())) {
      if (!salesOrderRequest.getAddress().isBlankAddress()) {
        OrderConsignee consignee = new OrderConsignee();
        BeanUtils.copyProperties(salesOrderRequest.getAddress(), consignee);
        consignee.setOrderId(orders.getId());
        orderConsigneeService.insert(consignee);
      }
    }

    if (orderDiscounts.size() > 0) {
      orderDiscounts.forEach(it -> it.setOrderId(orders.getId()));
      orderDiscountService.insertBatch(orderDiscounts);
    }
    return orders;
  }

  @Override public Orders selectByNo(String orderNo) {
    if (StringUtils.isBlank(orderNo)) {
      return null;
    }

    return this.baseMapper.selectByNo(orderNo);
  }

  /**
   * 后台获取销售订单列表
   */
  @Override
  public ResponseData<Page<IndexOrdersResponse>> backendIndex(int page, int size, IndexOrdersRequest request) {

    //判断该用户是否为超级管理员,如果为超级管理员则可以查询所有，如果不是超级管理员，则只能查询自己下的订单
    ResponseData responseData = salesUserService.getUserByToken();
    Map map = (HashMap) responseData.getData();
    String shopCode = null;
    if (map != null) {
      String shop = (String) map.get("shopCode");
      if (shop != null && !"".equals(shop)) {
        shopCode = shop;
      }
    }

    //判断时间格式是否正确
    String createdAt = request.getCreatedAt();
    String createdAtAfter = null;
    String createdAtBefore = null;
    if (createdAt != null && !"".equals(createdAt)) {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      boolean dateflag = true;
      try {
        Date date = format.parse(createdAt);
      } catch (ParseException e) {
        dateflag = false;
      }
      if (!dateflag) {
        return new ResponseData<>(ResponseBackCode.ERROR_PARAM_INVALID.getValue(), "日期格式无效，请输入yyyy-MM-dd格式");
      }
      createdAtBefore = createdAt + " 00:00:00";
      createdAtAfter = createdAt + " 23:59:59";
    }

    Page userPage = new Page<Orders>(page, size);
    userPage.setAsc(false);
    List<Orders> ordersList = baseMapper.backendIndex(userPage.getLimit(), userPage.getOffset(), createdAtBefore, createdAtAfter, shopCode, request);
    List<IndexOrdersResponse> responseList = new ArrayList<>();
    if (ordersList != null && ordersList.size() > 0) {
      for (int i = 0, length = ordersList.size(); i < length; i++) {
        IndexOrdersResponse response = new IndexOrdersResponse();
        Orders orders = ordersList.get(i);
        BeanUtils.copyProperties(orders, response);

        //设置状态
        response.setStatus(OrderStatus.getName(orders.getStatusId()));

        //会员是否为匿名
        String mobile = response.getMobile();
        if (OrderConstants.ORDER_MOBILE.getValue().equals(mobile)) {
          response.setMobile(OrderConstants.ORDER_MOBILE.getDescription());
        }

        responseList.add(response);
      }
    }
    //获取total数量
    List<Orders> totalList = baseMapper.backendIndex(null, null, createdAtBefore, createdAtAfter, shopCode, request);
    if (totalList != null && totalList.size() > 0) {
      userPage.setTotal(totalList.size());
    }

    userPage.setRecords(responseList);
    return new ResponseData<>(ResponseBackCode.SUCCESS.getValue(), ResponseBackCode.SUCCESS.getMessage(), userPage);
  }

  /**
   * 后台获取销售订单详情数据
   */
  @Override
  public ResponseData backendDetail(String no) {

    Map<String, Object> orderMap = new HashMap<>();
    orderMap.put("no", no);
    List<Orders> ordersList = selectByMap(orderMap);
    if (ordersList == null || ordersList.size() == 0) {
      return ResponseData.build(ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getValue(), ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getMessage(), "订单号无效");
    }

    Orders orders = baseMapper.backendDetail(no);
    DetailOrdersResponse response = new DetailOrdersResponse();
    BeanUtils.copyProperties(orders, response);

    //现金
    BigDecimal cashAmount = new BigDecimal(0);

    //在线支付
    BigDecimal onlinePayment = new BigDecimal(0);

    //找零
    BigDecimal giveChange = new BigDecimal(0);

    //支付信息表
    if (orders.getPaymentList() != null) {
      for (int i = 0, paylength = orders.getPaymentList().size(); i < paylength; i++) {
        PaymentResponse paymentResponse = new PaymentResponse();
        Payment payment = orders.getPaymentList().get(i);
        BeanUtils.copyProperties(payment, paymentResponse);

        if (PaymentConstants.CASHPAYMENT.getValue().equals(payment.getPartnerId())) {
          //现金支付
          cashAmount = cashAmount.add(payment.getPrice());
          giveChange = giveChange.add(payment.getGiveChange());
        } else {
          //在线支付
          onlinePayment = onlinePayment.add(payment.getPrice());
        }

        paymentResponse.setPaymentMethod(PaymentConstants.getDescriptionByValue(payment.getPartnerId()));
        response.getPaymentResponseList().add(paymentResponse);
      }
    }
    response.setCashAmount(cashAmount);
    response.setOnlinePayment(onlinePayment);
    response.setGiveChange(giveChange);

    LogisticResponse logisticResponse = new LogisticResponse();
    //是否含有配送信息
    if (orders.getOrderConsignee() != null) {
      OrderConsigneeResponse consignee = new OrderConsigneeResponse();
      BeanUtils.copyProperties(orders.getOrderConsignee(), consignee);

      //获取物料配送信息表里的物流公司和运单号
      Map<String, Object> expressMap = new HashMap<>();
      expressMap.put("order_no", orders.getNo());
      List<OrderExpress> expressList = orderExpressService.selectByMap(expressMap);
      if (expressList != null && expressList.size() > 0) {
        OrderExpress orderExpress = expressList.get(0);
        logisticResponse.setLogisticsCompany(orderExpress.getExpressCompany());
        logisticResponse.setWaybillNumber(orderExpress.getExpressNo());
      }
      consignee.getLogisticResponseList().add(logisticResponse);
      response.setOrderConsignee(consignee);
    }

    //会员是否为匿名
    String mobile = response.getMobile();
    if (OrderConstants.ORDER_MOBILE.getValue().equals(mobile)) {
      response.setMobile(OrderConstants.ORDER_MOBILE.getDescription());
    }

    //设置状态
    response.setStatus(OrderStatus.getName(orders.getStatusId()));

    //会员折扣优惠
    BigDecimal memberDiscount = (new BigDecimal(1).subtract(orders.getDiscount())).multiply(orders.getAmount());
    response.setMemberDiscount(memberDiscount);

    //从折扣表里查找订单优惠折扣
    Map<String, Object> discountMap = new HashMap();
    discountMap.put("order_id", orders.getId());
    List<OrderDiscount> orderDiscountList = orderDiscountService.selectByMap(discountMap);

    //优惠券金额
    BigDecimal couponAmount = new BigDecimal(0);

    //整单优惠金额
    BigDecimal wholeDiscountAmount = new BigDecimal(0);

    //积分抵扣
    BigDecimal pointsDiscount = new BigDecimal(0);

    if (orderDiscountList != null && orderDiscountList.size() > 0) {

      for (OrderDiscount orderDiscount : orderDiscountList) {
        //优惠券金额
        if (OrderDiscountTypeId.COUPON_MONEY.getValue().equals(orderDiscount.getTypeId())) {
          couponAmount = couponAmount.add(orderDiscount.getDiscountValue());
        }

        if (OrderDiscountTypeId.COUPON_DISCOUNT.getValue().equals(orderDiscount.getTypeId())) {
          couponAmount = couponAmount.add((new BigDecimal(1).subtract(orderDiscount.getDiscountValue())).multiply(orders.getAmount()));
        }

        //积分折算金额
        if (OrderDiscountTypeId.PONIT_MONEY.getValue().equals(orderDiscount.getTypeId())) {
          pointsDiscount = pointsDiscount.add(orderDiscount.getDiscountValue());
        }

        //整单优惠金额
        if (OrderDiscountTypeId.ALL_ORDERS.getValue().equals(orderDiscount.getTypeId())) {
          wholeDiscountAmount = wholeDiscountAmount.add(orderDiscount.getDiscountValue());
        }
      }
    }
    response.setCouponAmount(couponAmount);
    response.setPointsDiscount(pointsDiscount);
    response.setWholeDiscountAmount(wholeDiscountAmount);

    return ResponseData.build(ResponseBackCode.SUCCESS.getValue(), ResponseBackCode.SUCCESS.getMessage(), response);
  }

  /**
   * 前台整单优惠
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public ResponseData wholeDiscount(String no, WholeDiscountRequest request) {

    Map<String, Object> map = new HashMap<>();
    map.put("no", no);
    List<Orders> ordersList = selectByMap(map);
    if (ordersList == null || ordersList.size() == 0) {
      return ResponseData.build(ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getValue(), ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getMessage(), "订单号无效");
    }
    Orders orders = ordersList.get(0);

    //更改orders表里的payable_amout字段
    orders.setPayableAmount(request.getPayableAmount());
    updateById(orders);

    //在order_discount表里插入数据或者更新
    Map<String, Object> discountMap = new HashMap<>();
    discountMap.put("order_id", orders.getId());
    discountMap.put("type_id", OrderDiscountTypeId.ALL_ORDERS.getValue());
    List<OrderDiscount> discountList = orderDiscountService.selectByMap(discountMap);
    OrderDiscount orderDiscount = new OrderDiscount();
    if (discountList != null && discountList.size() > 0) {
      orderDiscount = discountList.get(0);
    } else {
      orderDiscount.setTypeId(OrderDiscountTypeId.ALL_ORDERS.getValue());
      orderDiscount.setOrderId(orders.getId());
    }
    orderDiscount.setDiscountValue(orders.getOriginalPayableAmount().subtract(request.getPayableAmount()));
    orderDiscountService.insertOrUpdate(orderDiscount);
    return ResponseData.build(ResponseBackCode.SUCCESS.getValue(), ResponseBackCode.SUCCESS.getMessage());
  }

  @Override public Page<ErpSalesOrderResponse> selectOrderForErpPage(
      Page<ErpSalesOrderResponse> page, ErpOrdersRequest request
  ) {
    List<Orders> orders = baseMapper.selectOrderForErpPage(page.getLimit(), page.getOffset(), request);
    if (orders.size() > 0) {
      List<ErpSalesOrderResponse> salesOrderResponses = new ArrayList<>(orders.size());
      HashMap<String, String> outerCodeMap = erpConfig.getAllAgencyMap();

      orders.forEach(it -> {
        ErpSalesOrderResponse orderVo = new ErpSalesOrderResponse();
        BeanUtils.copyProperties(it, orderVo);
        // 设置外部编码 也就是K3客户编码
        String outerCode = outerCodeMap.get(it.getAgencyCode());

        if (StringUtils.isBlank(outerCode)) {
          erpConfig.reloadAllAgencyMap();
        }

        orderVo.setOuterCode(outerCodeMap.get(it.getAgencyCode()));

        // 设置门店来源ID 用于erp表示门店
        String sourceKey = it.getAgencyCode() + it.getShopCode();
        orderVo.setSourceId(erpConfig.getAllAgencyAndShopNameAndSourceMap().get(sourceKey));

        if (Objects.isNull(erpConfig.getAllAgencyAndShopNameAndSourceMap().get(it.getAgencyCode())) ||
            Objects.isNull(erpConfig.getAllAgencyAndShopNameAndSourceMap().get(it.getShopCode()))) {

          // 重新加载
          erpConfig.reloadAllAgencyAndShopNameAndSourceMap();
        }

        orderVo.setIsPickup(it.getPickup());
        List<SalesOrderDetailResponse> orderDetails = new ArrayList<>(it.getOrderDetailList().size());

        it.getOrderDetailList().forEach(detail -> {

          String sku = detail.getSkuNo();
          //组合商品
          if(sku != null && sku.startsWith("TZ")){

            ResponseData responseData = materialService.getskuBygroupNumber(sku);
            List<MaterialResponse> materialResponses = JsonTransformer
                .getObjectMapper()
                .convertValue(responseData.getData(), new TypeReference<List<MaterialResponse>>() {});
            if(materialResponses != null && !materialResponses.isEmpty()) {

                for (int i = 0; i < materialResponses.size(); i++) {
                  MaterialResponse materialResponse = materialResponses.get(i);

                  SalesOrderDetailResponse detailVo = new SalesOrderDetailResponse();
                  BeanUtils.copyProperties(materialResponse, detailVo);

                  //原价
                  BigDecimal original = detail.getOriginalAmount();
                  //真实价格
                  BigDecimal real = detail.getRealSingleAmount();

                  BigDecimal subtotal = detail.getSubtotal();
                  if(new BigDecimal(0).compareTo(original) == 0){
                    detailVo.setRealSingleAmount(new BigDecimal(0));
                    detailVo.setSubtotal(new BigDecimal(0));
                  }else{
                    detailVo.setRealSingleAmount(materialResponse.getAmount().multiply(real).divide(original,BigDecimal.ROUND_HALF_UP));
                    detailVo.setSubtotal(materialResponse.getAmount().multiply(subtotal).divide(original,BigDecimal.ROUND_HALF_UP));
                  }

                  int quantity = detail.getQuantity()*materialResponse.getStockQty();
                  detailVo.setRealAmount(detailVo.getRealSingleAmount().multiply(new BigDecimal(quantity)));
                  detailVo.setDetailNo(detail.getDetailNo());
                  detailVo.setQuantity(quantity);
                  detailVo.setIsPickup(detail.getPickup());
                  detailVo.setIsSample(detail.getSample());
                  detailVo.setIsGift(detail.getGift());
                  detailVo.setTzTotal(detail.getSubtotal());
                  detailVo.setTzRealAmount(detail.getRealAmount());
                  orderDetails.add(detailVo);
              }

            }
          }else{
            SalesOrderDetailResponse orderDetailVo = new SalesOrderDetailResponse();
            BeanUtils.copyProperties(detail, orderDetailVo);
            orderDetailVo.setIsPickup(detail.getPickup());
            orderDetailVo.setIsSample(detail.getSample());
            orderDetailVo.setIsGift(detail.getGift());
            orderDetails.add(orderDetailVo);
          }
        });
        orderVo.setOrderDetails(orderDetails);

        if (!orderVo.getIsPickup()) {
          // 非自提单时 必须有配送地址

          if (Objects.isNull(it.getOrderConsignee())) {
            // 如果这里为空，肯定是创建销售订单时没正确设置配送信息
            logger().error("销售订单({})配送信息有问题，请检查创建销售订单时添加配送信息逻辑", orderVo.getNo());
            // return 在lambda里中return相当于 for循环的continue;
            return;
          }

          OrderConsigneeVo consigneeVo = new OrderConsigneeVo();
          BeanUtils.copyProperties(it.getOrderConsignee(), consigneeVo);
          orderVo.setOrderConsignee(consigneeVo);
        }

        salesOrderResponses.add(orderVo);
      });

      page.setRecords(salesOrderResponses);

      Integer total = baseMapper.selectOrderForErpPageCount(request);

      page.setTotal(total);
    }

    return page;
  }

  /**
   * 确认收银
   */
  @Transactional(rollbackFor = Exception.class)
  @Override public ResponseData confirmPayment(String no, ConfirmPaymentRequest request) {

    //查找订单号是否存在
    Map<String, Object> orderMap = new HashMap<>();
    orderMap.put("no", no);
    List<Orders> ordersList = selectByMap(orderMap);
    if (ordersList == null || ordersList.size() == 0) {
      return ResponseData.build(ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getValue(), ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getMessage(), "订单号无效");
    }

    //更新orders表
    Orders orders = ordersList.get(0);
    BigDecimal paidAmount = orders.getPaidAmount(); //实收
    BigDecimal payableAmount = request.getPayableAmount(); //支付金额

    //现金支付
    Payment payment = new Payment();

    payment.setOrderId(orders.getId());
    payment.setPaidAt(LocalDateTime.now());
    payment.setPrice(payableAmount);
    if (orders.getPayableAmount().compareTo(orders.getPaidAmount()==null?new BigDecimal(0):orders.getPaidAmount()) == 0) {
      payment.setStatusId(OrderStatus.PAY_SUCCESSFULLY.getCode());
    }else{
      payment.setStatusId(OrderStatus.WAIT_PAY.getCode());
    }
    payment.setCashierId(request.getCreaterId());
    if (PaymentConstants.CASHPAYMENT.getValue().equals(request.getPaymentType()) || PaymentConstants.OTHERSPAYMENT.getValue().equals(request.getPaymentType())) {
      //更新payment表
      payment.setGiveChange(request.getGiveChange());
      payment.setPartnerId(request.getPaymentType());
      payment.setRealReceivePrice(payableAmount);
      payableAmount = payableAmount.subtract(request.getGiveChange()==null?new BigDecimal(0):request.getGiveChange());
      ResponseData responseData = getPaymentCode(no);
      payment.setPaymentNo((String) responseData.getData());
    } else {
      //刷卡
      if (PaymentConstants.POSPAYMENT.getValue().equals(request.getPaymentType())) {
        payment.setPartnerId(PaymentConstants.POSPAYMENT.getValue());
      } else {
        //扫码支付
        String cups = request.getCups();
        if (cups != null && PaymentConstants.WEIXIN_PAYMENT.getValue().equals(cups)) {
          //微信
          payment.setPartnerId(PaymentConstants.WEIXIN_PAYMENT.getValue());
        } else if (cups != null && PaymentConstants.ALI_PAYMENT.getValue().equals(cups)) {
          //支付宝
          payment.setPartnerId(PaymentConstants.ALI_PAYMENT.getValue());
        } else {
          //扫码
          payment.setPartnerId(PaymentConstants.SCANPAYMENT.getValue());
        }
      }

      if (request.getTraceNo() != null && !request.getTraceNo().isEmpty()) {
        payment.setPartnerTradeNo(request.getTraceNo());
      }
      if (request.getMerchId() != null && !request.getMerchId().isEmpty()) {
        payment.setMerchantNumber(request.getMerchId());
      }
      if (request.getMerchName() != null && !request.getMerchName().isEmpty()) {
        payment.setMerchantName(request.getMerchName());
      }
      if (request.getCardno() != null && !request.getCardno().isEmpty()) {
        payment.setAccount(request.getCardno());
      }
      if (request.getPaymentTransactionCode() != null && !request.getPaymentTransactionCode().isEmpty()) {
        payment.setPaymentNo(request.getPaymentTransactionCode());
      } else {
        ResponseData responseData = getPaymentCode(no);
        payment.setPaymentNo((String) responseData.getData());
      }
      payment.setRealReceivePrice(payableAmount);
    }
    //更新orders表
    if (paidAmount != null) {
      orders.setPaidAmount(paidAmount.add(payableAmount));//已收金额
    } else {
      orders.setPaidAmount(payableAmount);//已收金额
    }

    orders.setPaidAt(LocalDateTime.now());//付款时间
    if (orders.getPayableAmount().compareTo(orders.getPaidAmount()) == 0) {
      orders.setStatusId(OrderStatus.PAY_SUCCESSFULLY.getCode());
    }
    updateById(orders);

    //设置实际应收金额和实际应收单价
    BigDecimal payable = orders.getPayableAmount(); //整单应收价格
    Map<String, Object> orderDetailMap = new HashMap<>();
    orderDetailMap.put("order_id", orders.getId());
    List<OrderDetail> orderDetailList = orderDetailService.selectByMap(orderDetailMap);
    if (orderDetailList != null && orderDetailList.size() > 0) {
      BigDecimal amountTotal = new BigDecimal(0); //现价总价
      List<OrderDetail> orderDetails = new ArrayList<>();
      for (OrderDetail orderDetail : orderDetailList) {
        BigDecimal quantity = new BigDecimal(orderDetail.getQuantity());
        BigDecimal amount = orderDetail.getAmount().multiply(quantity);
        amountTotal = amountTotal.add(amount);
      }
      if (amountTotal.compareTo(new BigDecimal(0)) > 0) {
        for (OrderDetail orderDetail : orderDetailList) {
          BigDecimal quantity = new BigDecimal(orderDetail.getQuantity());
          orderDetail.setRealAmount(orderDetail.getAmount().multiply(quantity).divide(amountTotal, MathContext.DECIMAL128).multiply(payable));
          orderDetail.setRealSingleAmount(orderDetail.getAmount().divide(amountTotal, MathContext.DECIMAL128).multiply(payable));
          orderDetails.add(orderDetail);
        }
        orderDetailService.updateBatchById(orderDetails);
      }
    }

    //更新payment表
    paymentService.insert(payment);

    updateCanReturnParam(orders, orderDetailList);

    return ResponseData.build(ResponseBackCode.SUCCESS.getValue(), ResponseBackCode.SUCCESS.getMessage(), "收银成功");
  }

  /**
   * 更新可退货退款参数 如更新订单Order表中refund_amount、更新订单详情order_detail表中的refund_amount和refund_quantity参数
   */
  private void updateCanReturnParam(Orders orders, List<OrderDetail> orderDetailList) {
    Orders tempOrder = new Orders();
    tempOrder.setId(orders.getId());
    tempOrder.setRefundAmount(orders.getPaidAmount());
    this.updateById(tempOrder);

    if (orders.getPaidAmount().compareTo(orders.getPayableAmount()) == 0) {
      List<OrderDetail> tempOrderDetailList = new ArrayList<>(orderDetailList.size());

      orderDetailList.forEach(it -> {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(it.getId());
        orderDetail.setRefundAmount(it.getRealAmount());
        orderDetail.setRefundQuantity(it.getQuantity());

        tempOrderDetailList.add(orderDetail);
      });

      orderDetailService.updateBatchById(tempOrderDetailList);
    }
  }

  /**
   * 备注
   */
  @Override public ResponseData editNotes(String no, EditNotesRequest request) {
    //查找订单号是否存在
    Map<String, Object> orderMap = new HashMap<>();
    orderMap.put("no", no);
    List<Orders> ordersList = selectByMap(orderMap);
    if (ordersList == null || ordersList.size() == 0) {
      return ResponseData.build(ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getValue(), ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getMessage(), "订单号无效");
    }

    //更新orders表
    Orders orders = ordersList.get(0);
    orders.setNote(request.getNote());
    updateById(orders);
    return ResponseData.build(ResponseBackCode.SUCCESS.getValue(), ResponseBackCode.SUCCESS.getMessage(), "修改备注成功");
  }

  /**
   * 取消订单
   */
  @Transactional(rollbackFor = Exception.class)
  @Override public ResponseData cancelOrder(String no) {
    //查找订单号是否存在
    Map<String, Object> orderMap = new HashMap<>();
    orderMap.put("no", no);
    List<Orders> ordersList = selectByMap(orderMap);
    if (ordersList == null || ordersList.size() == 0) {
      return ResponseData.build(ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getValue(), ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getMessage(), "订单号无效");
    }

    //更新orders表
    Orders orders = ordersList.get(0);
    orders.setStatusId(OrderStatus.CANCELED.getCode());
    Boolean isSuccess = updateById(orders);
    if (isSuccess) {
      // 取消订单 要返还积分
      int returnPoint = Math.abs(orders.getUsedPoint());
      if (returnPoint > 0) {
        //返还积分
        sipinService.updatePoint(orders.getMobile(), returnPoint, PointType.INCREASE.getValue(), orders.getNo());
        logger().info("用户(手机号：{})，取消订单({})，返还积分:{}", orders.getMobile(), orders.getNo(), returnPoint);
      }

      OrderDiscount orderDiscount = orderDiscountService.selectOrderCouponByOrderId(orders.getId());
      if (!Objects.isNull(orderDiscount)) {
        sipinService.resumeCoupon(orderDiscount.getCode());
      }
    }

    return ResponseData.build(ResponseBackCode.SUCCESS.getValue(), ResponseBackCode.SUCCESS.getMessage(), "取消订单成功");
  }

  /**
   * 确认完成
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public ResponseData confirmComplete(String no) {
    //查找订单号是否存在
    Map<String, Object> orderMap = new HashMap<>();
    orderMap.put("no", no);
    List<Orders> ordersList = selectByMap(orderMap);
    if (ordersList == null || ordersList.size() == 0) {
      return ResponseData.build(ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getValue(), ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getMessage(), "订单号无效");
    }

    //更新orders表
    Orders orders = ordersList.get(0);
    orders.setStatusId(OrderStatus.FINISHED.getCode());
    // 设置已给积分 再下面updatePoint会同步积分到商城
    orders.setGivePoint(orders.getPaidAmount().intValue());
    Boolean isSuccess = updateById(orders);

    // 设置可退数量和可退金额
    List<OrderDetail> orderDetails = orderDetailService.selectList(new EntityWrapper<>(new OrderDetail(orders.getId())));
    orderDetails.forEach(it -> {
      it.setRefundQuantity(it.getQuantity());
      it.setRefundAmount(it.getRealAmount());
    });

    orderDetailService.updateBatchById(orderDetails);

    if (isSuccess && !orders.getMobile().equals(ANONYMOUS_USER)) {
      //增加积分
      sipinService.updatePoint(orders.getMobile(), orders.getPaidAmount().intValue(), PointType.INCREASE.getValue(), orders.getNo());
    }

    return ResponseData.build(ResponseBackCode.SUCCESS.getValue(), ResponseBackCode.SUCCESS.getMessage(), "收银成功");
  }

  /**
   * 得到支付交易码
   */
  @Override public ResponseData getPaymentCode(String no) {
    boolean isSame = true;
    String paymentCode = GenerateDistributedID.getSalesOrderNo(no);
    Map<String, Object> map = new HashMap<>();
    map.put("payment_no", paymentCode);
    List<Payment> paymentList = paymentService.selectByMap(map);
    if (paymentList == null || paymentList.size() == 0) {
      return ResponseData.build(ResponseBackCode.SUCCESS.getValue(), ResponseBackCode.SUCCESS.getMessage(), paymentCode);
    }

    while (isSame) {
      paymentCode = GenerateDistributedID.getSalesOrderNo(no);
      map.put("payment_no", paymentCode);
      paymentList = paymentService.selectByMap(map);
      if (paymentList == null || paymentList.size() == 0) {
        isSame = false;
      }
    }
    return ResponseData.build(ResponseBackCode.SUCCESS.getValue(), ResponseBackCode.SUCCESS.getMessage(), paymentCode);
  }

  /**
   * 后台导出excel表单
   */
  @Override
  public ResponseData exportExcel(IndexOrdersRequest request) {

    //判断该用户是否为超级管理员,如果为超级管理员则可以查询所有，如果不是超级管理员，则只能查询自己下的订单
    ResponseData responseData = salesUserService.getUserByToken();
    Map map = (HashMap) responseData.getData();
    String shopCode = null;
    if (map != null) {
      String shop = (String) map.get("shopCode");
      if (shop != null && !"".equals(shop)) {
        shopCode = shop;
      }
    }

    //判断时间格式是否正确
    String createdAt = request.getCreatedAt();
    String createdAtAfter = null;
    String createdAtBefore = null;
    if (createdAt != null && !"".equals(createdAt)) {
      createdAtBefore = createdAt + " 00:00:00";
      createdAtAfter = createdAt + " 23:59:59";
    }

    List<Orders> ordersList = baseMapper.backendIndex(null, null, createdAtBefore, createdAtAfter, shopCode, request);

    List<IndexOrdersResponse> responseList = new ArrayList<>();
    if (ordersList != null && ordersList.size() > 0) {
      for (int i = 0, length = ordersList.size(); i < length; i++) {
        IndexOrdersResponse response = new IndexOrdersResponse();
        Orders orders = ordersList.get(i);
        BeanUtils.copyProperties(orders, response);

        //设置状态
        response.setStatus(OrderStatus.getName(orders.getStatusId()));

        //会员是否为匿名
        String mobile = response.getMobile();
        if (OrderConstants.ORDER_MOBILE.getValue().equals(mobile)) {
          response.setMobile(OrderConstants.ORDER_MOBILE.getDescription());
        }

        responseList.add(response);
      }
    }
    return ResponseData.build(ResponseBackCode.SUCCESS.getValue(), ResponseBackCode.SUCCESS.getMessage(), responseList);
  }
}
