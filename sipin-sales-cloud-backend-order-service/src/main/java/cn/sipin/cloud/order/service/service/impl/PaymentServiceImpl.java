package cn.sipin.cloud.order.service.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sipin.cloud.order.service.config.ErpConfig;
import cn.sipin.cloud.order.service.feign.impl.SalesUserServiceImpl;
import cn.sipin.cloud.order.service.feign.service.SalesUserService;
import cn.sipin.cloud.order.service.mapper.PaymentMapper;
import cn.sipin.cloud.order.service.service.PaymentServiceContract;
import cn.sipin.cloud.order.service.util.GenerateDistributedID;
import cn.sipin.cloud.order.service.util.JsonTransformer;
import cn.sipin.cloud.order.service.util.UserLoginUtil;
import cn.sipin.sales.cloud.order.constants.OrderStatus;
import cn.sipin.sales.cloud.order.constants.PaymentConstants;
import cn.sipin.sales.cloud.order.pojo.Orders;
import cn.sipin.sales.cloud.order.pojo.Payment;
import cn.sipin.sales.cloud.order.request.payment.backend.IndexSalesPaymentRequest;
import cn.sipin.sales.cloud.order.request.payment.index.IndexPaymentRequest;
import cn.sipin.sales.cloud.order.request.payment.index.IndexUserRequest;
import cn.sipin.sales.cloud.order.request.payment.sum.SumPaymentRequest;
import cn.sipin.sales.cloud.order.response.backend.index.IndexSalesPaymentResponse;
import cn.sipin.sales.cloud.order.response.front.index.IndexPaymentResponse;
import cn.sipin.sales.cloud.order.response.front.index.SalesUser;
import cn.sipin.sales.cloud.order.response.front.index.SalesUserResponse;
import cn.sipin.sales.cloud.order.response.front.sum.SumPayment;
import cn.sipin.sales.cloud.order.response.front.sum.SumPaymentResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

/**
 * 支付流水信息表 服务实现类
 */
@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentServiceContract {

  private SalesUserService salesUserService;

  private UserLoginUtil userLoginUtil;

  private SalesUserServiceImpl salesUserServiceImpl;

  private ErpConfig erpConfig;

  @Autowired
  public PaymentServiceImpl(SalesUserService salesUserService,UserLoginUtil userLoginUtil,SalesUserServiceImpl salesUserServiceImpl,
                            ErpConfig erpConfig){
    this.salesUserService = salesUserService;
    this.userLoginUtil = userLoginUtil;
    this.salesUserServiceImpl = salesUserServiceImpl;
    this.erpConfig = erpConfig;
  }

  @Override
  public ResponseData<Page<IndexPaymentResponse>> index(
      int page, int size, IndexPaymentRequest request) {
    Page userPage = new Page<Orders>(page, size);
    userPage.setAsc(false);

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

    List<Payment> paymentList = baseMapper.index(userPage.getLimit(), userPage.getOffset(), shopCode, request);

    //获取总共的数量
    List<Payment> totalPaymentList = baseMapper.index(null, null, shopCode, request);
    List<IndexPaymentResponse> responseList = new ArrayList<>();

    //判断是否有支付列表数据
    if(paymentList != null && !paymentList.isEmpty()){

      //查找该门店下的所有用户
      IndexUserRequest indexUserRequest = new IndexUserRequest();
      indexUserRequest.setShopCode(shopCode);
      ResponseData userResponseData =  salesUserService.indexUser(1,1000,indexUserRequest);
      SalesUserResponse salesUserResponse = JsonTransformer
          .getObjectMapper()
          .convertValue(userResponseData.getData(), new TypeReference<SalesUserResponse>() {});
      Map<String,String> userMap = new HashMap<>();
      if(salesUserResponse != null && salesUserResponse.getRecords() != null && !salesUserResponse.getRecords().isEmpty()){
        List<SalesUser> salesUserList = salesUserResponse.getRecords();
        for (SalesUser salesUser:salesUserList){
          userMap.put(salesUser.getId(),salesUser.getName());
        }
      }

      IndexPaymentResponse response;
      for (Payment payment:paymentList){
        response = new IndexPaymentResponse();
        BeanUtils.copyProperties(payment,response);

        //判断是否是收款还是退款，根据状态判断
        if(payment.getStatusId() != null && OrderStatus.WAIT_RETURN_MONEY.getCode().equals(payment.getStatusId())){
          response.setExchangeType(PaymentConstants.REBACK_PAYMENT.getDescription());
        }else{
          response.setExchangeType(PaymentConstants.RECEIVE_PAYMENT.getDescription());
        }

        //交付方式
        response.setExchangeMode(PaymentConstants.getDescriptionByValue(payment.getPartnerId()));


        //从redis中查找该用户的用户名
        response.setOperator(userMap.get(payment.getCashierId()+""));

        responseList.add(response);
      }
    }

    //设置总条数
    if(totalPaymentList != null && !totalPaymentList.isEmpty()){
      userPage.setTotal(totalPaymentList.size());
    }

    userPage.setRecords(responseList);
    return new ResponseData<Page<IndexPaymentResponse>>(ResponseBackCode.SUCCESS.getValue(), ResponseBackCode.SUCCESS.getMessage(), userPage);
  }

  @Override
  public ResponseData<SumPaymentResponse> sum(SumPaymentRequest request) {

    Page userPage = new Page<Orders>(1, 100);
    userPage.setAsc(false);

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

    //收款列表
    List<Payment> receiveList = baseMapper.sumPayment(null, null, OrderStatus.WAIT_RETURN_MONEY.getCode(),null, shopCode,request);

    //退款列表
    List<Payment> backList = baseMapper.sumPayment(null, null, null,OrderStatus.WAIT_RETURN_MONEY.getCode(),shopCode, request);

    //返回到前端
    SumPaymentResponse response = new SumPaymentResponse();

    //总累计
    Long totalCount=0L;
    BigDecimal totalPrice = new BigDecimal(0);

    //收款
    if(receiveList != null && !receiveList.isEmpty()){
      List<SumPayment> receivePaymentList = new ArrayList<>(receiveList.size()+1);
      combinateList(receivePaymentList,receiveList);
      totalCount +=  receivePaymentList.get(receivePaymentList.size()-1).getTotal();
      totalPrice = totalPrice.add(receivePaymentList.get(receivePaymentList.size()-1).getPrice());
      response.setReceivePaymentList(receivePaymentList);
    }
    //退款
    if(backList != null && !backList.isEmpty()){
      List<SumPayment> backPaymentList = new ArrayList<>(backList.size()+1);
      combinateList(backPaymentList,backList);
      totalCount +=  backPaymentList.get(backPaymentList.size()-1).getTotal();
      totalPrice = totalPrice.add(backPaymentList.get(backPaymentList.size()-1).getPrice());
      response.setBackPaymentList(backPaymentList);
    }
    response.setTotalCount(totalCount);
    response.setTotalPrice(totalPrice);
    return  new ResponseData<>(ResponseBackCode.SUCCESS.getValue(), ResponseBackCode.SUCCESS.getMessage(),response);

  }

  /**
   * 退款更新支付表
   * @param returnType 退款方式
   * @param amount 退货金额
   * @param orderId 订单id
   */
  @Override
  public boolean returnPayment(String returnType, BigDecimal amount,Long orderId) {
    Payment payment = new Payment();

    //设置订单id
    payment.setOrderId(orderId);

    //设置收银员帐号
    Long cashierId  = userLoginUtil.getUserId();
    payment.setCashierId(cashierId);

    //设置退款交易流水号
    String paymentCode = getPaymentCode(orderId+"");
    if(paymentCode !=null && !paymentCode.isEmpty()){
      payment.setPaymentNo(paymentCode);
    }

    //设置退款时间、创建时间、退款状态、退款金额、退款方式
    payment.setPaidAt(LocalDateTime.now());
    payment.setCreatedAt(LocalDateTime.now());
    payment.setStatusId(OrderStatus.WAIT_RETURN_MONEY.getCode());
    payment.setRealReceivePrice(amount.multiply(new BigDecimal(-1)));
    payment.setPartnerId(returnType);

    insert(payment);

    return true;
  }

  @Override public ResponseData<Page<IndexSalesPaymentResponse>> backIndex(
      Integer page, Integer size, IndexSalesPaymentRequest request,boolean flag
  ) {
    Page userPage = new Page<Orders>(page, size);
    userPage.setAsc(false);

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

    Integer exchangeType=null;
    if(request.getExchangeType() != null){
      if(PaymentConstants.EXCHANGE_TYPE.getValue().equals(request.getExchangeType())){
        exchangeType = Integer.parseInt(PaymentConstants.EXCHANGE_TYPE.getDescription());
      }
    }
    List<Payment> paymentList = null;
    if(flag){
      paymentList = baseMapper.backendIndex(userPage.getLimit(), userPage.getOffset(), exchangeType, request);
    }else{
      paymentList = baseMapper.backendIndex(null, null, exchangeType, request);
    }


    //获取总共的数量
    List<Payment> totalPaymentList = baseMapper.backendIndex(null, null, exchangeType, request);
    List<IndexSalesPaymentResponse> responseList = new ArrayList<>();

    //判断是否有支付列表数据
    if(paymentList != null && !paymentList.isEmpty()){

      //查找该门店下的所有用户
      Map<Long,String> userMap =  salesUserServiceImpl.getUserInfoList();



      IndexSalesPaymentResponse response;
      for (Payment payment:paymentList){
        response = new IndexSalesPaymentResponse();
        BeanUtils.copyProperties(payment,response);

        response.setAgencyName(erpConfig.getAllAgencyAndShopNameAndSourceMap().get(response.getAgencyCode()));
        response.setShopName(erpConfig.getAllAgencyAndShopNameAndSourceMap().get(response.getShopCode()));

        //判断是否是收款还是退款，根据状态判断
        if(payment.getStatusId() != null && OrderStatus.WAIT_RETURN_MONEY.getCode().equals(payment.getStatusId())){
          response.setExchangeType(PaymentConstants.REBACK_PAYMENT.getDescription());
        }else{
          response.setExchangeType(PaymentConstants.RECEIVE_PAYMENT.getDescription());
        }

        //交付方式
        response.setExchangeMode(PaymentConstants.getDescriptionByValue(payment.getPartnerId()));


        //从redis中查找该用户的用户名
        response.setOperator(userMap.get(payment.getCashierId()));

        responseList.add(response);
      }
    }

    //设置总条数
    if(totalPaymentList != null && !totalPaymentList.isEmpty()){
      userPage.setTotal(totalPaymentList.size());
    }

    userPage.setRecords(responseList);
    return new ResponseData<Page<IndexSalesPaymentResponse>>(ResponseBackCode.SUCCESS.getValue(), ResponseBackCode.SUCCESS.getMessage(), userPage);

  }

  /**
   * 设置退款流水号
   */
  private String getPaymentCode(String orderId) {
    boolean isSame = true;
    String paymentCode = GenerateDistributedID.getSalesOrderNo(orderId);
    Map<String, Object> map = new HashMap<>();
    map.put("payment_no", paymentCode);
    List<Payment> paymentList = selectByMap(map);
    if (paymentList == null || paymentList.size() == 0) {
      return paymentCode;
    }

    while (isSame) {
      paymentCode = GenerateDistributedID.getReturnPaymentNo(orderId);
      map.put("payment_no", paymentCode);
      paymentList = selectByMap(map);
      if (paymentList == null || paymentList.size() == 0) {
        isSame = false;
      }
    }
    return paymentCode;
  }

  private void combinateList(List<SumPayment> resultList,List<Payment> list){
    SumPayment sumPayment;
    Long totalCount=0L;
    BigDecimal totalPrice = new BigDecimal(0);
    for (Payment payment:list){
      sumPayment = new SumPayment();
      sumPayment.setPrice(payment.getRealReceivePrice());
      sumPayment.setTotal(payment.getId());
      sumPayment.setType(PaymentConstants.getDescriptionByValue(payment.getPartnerId()));
      resultList.add(sumPayment);
      totalPrice = totalPrice.add(payment.getRealReceivePrice());
      totalCount += sumPayment.getTotal();
    }
    sumPayment = new SumPayment();
    sumPayment.setType(PaymentConstants.COUNT_PAYMENT.getDescription());
    sumPayment.setPrice(totalPrice);
    sumPayment.setTotal(totalCount);
    resultList.add(sumPayment);
  }

}
