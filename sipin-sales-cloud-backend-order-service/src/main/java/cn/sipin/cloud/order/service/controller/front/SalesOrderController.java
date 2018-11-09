package cn.sipin.cloud.order.service.controller.front;

import com.baomidou.mybatisplus.plugins.Page;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import cn.sipin.cloud.order.service.feign.impl.MaterialServiceImpl;
import cn.sipin.cloud.order.service.feign.impl.SalesUserServiceImpl;
import cn.sipin.cloud.order.service.service.OrdersServiceContract;
import cn.sipin.cloud.order.service.service.PointServiceContract;
import cn.sipin.cloud.order.service.service.RedisClusterServiceContract;
import cn.sipin.cloud.order.service.service.SipinServiceContract;
import cn.sipin.cloud.order.service.sipin.constant.CouponScopeEnum;
import cn.sipin.cloud.order.service.sipin.constant.CouponStatusEnum;
import cn.sipin.cloud.order.service.sipin.vo.member.MemberApi;
import cn.sipin.cloud.order.service.sipin.vo.member.MemberApiCoupon;
import cn.sipin.sales.cloud.order.constants.RedisConstants;
import cn.sipin.sales.cloud.order.pojo.OrderDetail;
import cn.sipin.sales.cloud.order.pojo.Orders;
import cn.sipin.sales.cloud.order.request.SalesOrderRequest;
import cn.sipin.sales.cloud.order.request.backend.index.IndexOrdersRequest;
import cn.sipin.sales.cloud.order.request.confirmPayment.ConfirmPaymentRequest;
import cn.sipin.sales.cloud.order.request.editNotes.EditNotesRequest;
import cn.sipin.sales.cloud.order.request.wholeDiscount.WholeDiscountRequest;
import cn.sipin.sales.cloud.order.response.AgencyCodeResponse;
import cn.sipin.sales.cloud.order.response.SalesOrderResponse;
import cn.sipin.sales.cloud.order.response.backend.index.IndexOrdersResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import cn.siyue.platform.exceptions.exception.RequestException;
import cn.siyue.platform.httplog.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 销售订单 前端控制器
 * </p>
 */
@RestController
@Api(tags = "前台销售订单")
@RequestMapping(path = "/front/orders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SalesOrderController {

  private final static Logger logger = LoggerFactory.getLogger(SalesOrderController.class);

  private OrdersServiceContract ordersService;

  private RedisClusterServiceContract redisService;

  private MaterialServiceImpl materialService;

  private SalesUserServiceImpl salesUserService;

  private SipinServiceContract sipinService;

  private PointServiceContract pointService;

  @Autowired
  public SalesOrderController(
      OrdersServiceContract ordersService, RedisClusterServiceContract redisService, MaterialServiceImpl materialService,
      SalesUserServiceImpl salesUserService,
      SipinServiceContract sipinService,
      PointServiceContract pointService
  ) {
    this.ordersService = ordersService;
    this.redisService = redisService;
    this.materialService = materialService;
    this.salesUserService = salesUserService;
    this.sipinService = sipinService;
    this.pointService = pointService;
  }

  @LogAnnotation
  @ApiOperation(nickname = "frontCreateOrder", value = "前台新建销售单")
  @PostMapping("/create")
  public ResponseData<SalesOrderResponse> store(@RequestBody SalesOrderRequest salesOrderRequest) {
    if (redisService.existKey(RedisConstants.SALES_ORDER_UNIQUE + salesOrderRequest.getSessionKey())) {
      redisService.del(RedisConstants.SALES_ORDER_UNIQUE + salesOrderRequest.getSessionKey());
    } else {
      return new ResponseData<SalesOrderResponse>(
          ResponseBackCode.ERROR_CREATE_FAIL.getValue(),
          ResponseBackCode.ERROR_CREATE_FAIL.getMessage() + "：重复请求或session不存在"
      );
    }

    // 经销商信息-异步
    Future<ResponseData> future = null;
    Boolean isAsyn = true;
    try {
      // 异步操作
      future = salesUserService.getUserFutureByToken();
    } catch (RejectedExecutionException e) {
      logger.warn("异常信息：RejectedExecutionException" + e.getMessage());
      isAsyn = false;
    }

    // 获取物料列表
    List<OrderDetail> orderDetailList = null;
    try {
      orderDetailList = materialService.getSkus(salesOrderRequest.getOrderSkuDetails());
    } catch (Exception e) {
      e.printStackTrace();
    }

    // 验证优惠券和积分有效性
    MemberApi member = null;

    MemberApiCoupon couponValue = null;
    if (StringUtils.isNotBlank(salesOrderRequest.getMobile())) {
      // 获取非匿名用户信息，积分及优惠券及会员等级
      // 检查是否有用优惠券及积分 有则判断
      member = sipinService.getMember(salesOrderRequest.getMobile());
      if (StringUtils.isNotBlank(salesOrderRequest.getCouponCode())) {
        couponValue = checkAvailableCouponAndGetCoupon(member, orderDetailList, salesOrderRequest.getCouponCode());
      }

      if (!Objects.isNull(salesOrderRequest.getPoint()) && salesOrderRequest.getPoint() > 0) {
        if (member.getPoint().compareTo(salesOrderRequest.getPoint()) < 0) {
          return new ResponseData<SalesOrderResponse>(
              ResponseBackCode.ERROR_PARAM_INVALID.getValue(),
              ResponseBackCode.ERROR_PARAM_INVALID.getMessage() + "积分不够"
          );
        }
      }
    }

    // 获取经销商信息
    AgencyCodeResponse agencyInfoVo = null;
    if (isAsyn) {
      // 异步获取
      agencyInfoVo = salesUserService.getAgencyCodeResponseByFuture(future);
    } else {
      // 得到经销商信息 同步操作
      agencyInfoVo = salesUserService.getUserByToken();
    }

    Orders orders = null;
    try {
      orders = ordersService.create(salesOrderRequest, agencyInfoVo, member, orderDetailList, couponValue);
    } catch (Exception e) {
      e.printStackTrace();
    }

    SalesOrderResponse response = new SalesOrderResponse();
    BeanUtils.copyProperties(orders, response);
    return new ResponseData<SalesOrderResponse>(
        ResponseBackCode.SUCCESS.getValue(),
        ResponseBackCode.SUCCESS.getMessage(),
        response
    );
  }

  /**
   * 检查订单商品 是否满足优惠券规则 并返回该优惠券
   */
  private MemberApiCoupon checkAvailableCouponAndGetCoupon(MemberApi member, List<OrderDetail> orderDetails, String coupnCode) {

    MemberApiCoupon[] coupons = member.getCoupon();
    MemberApiCoupon usedCoupon = null;
    for (MemberApiCoupon coupon : coupons) {
      if (coupon.getCode().equals(coupnCode)) {
        usedCoupon = coupon;
        break;
      }
    }

    if (Objects.isNull(usedCoupon)) {
      throw new RequestException(ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getValue(), "用户没有该优惠券");
    }

    if (!CouponStatusEnum.AVAILABLE.equals(usedCoupon.getStatus())) {
      throw new RequestException(ResponseBackCode.ERROR_PARAM_INVALID.getValue(), "用户该优惠券过期或不可用");
    }

    if (CouponScopeEnum.OVERALL.equals(usedCoupon.getTask().getScope())) {
      // 全场
      // 总价
      BigDecimal amountTotal = orderDetails.stream()
          .map(it -> it.getAmount().multiply(new BigDecimal(it.getQuantity().toString())))
          .reduce(BigDecimal.ZERO, BigDecimal::add);
      if (amountTotal.compareTo(usedCoupon.getTask().getThreshold()) < 0) {
        throw new RequestException(ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getValue(), "订单总价低于优惠券满减满折数");
      }
    } else {
      // 指定商品
      HashSet<String> hashSet = new HashSet<String>(Arrays.asList(usedCoupon.getTask().getGoodsSkuSn()));
      BigDecimal amountTotal = orderDetails.stream()
          .filter(it -> hashSet.contains(it.getSkuSn()))
          .map(it -> it.getAmount().multiply(new BigDecimal(it.getQuantity().toString())))
          .reduce(BigDecimal.ZERO, BigDecimal::add);
      if (amountTotal.compareTo(usedCoupon.getTask().getThreshold()) < 0) {
        throw new RequestException(ResponseBackCode.ERROR_OBJECT_NOT_EXIST.getValue(), "指定商品总价低于优惠券满减满折数");
      }
    }

    return usedCoupon;
  }

  /**
   * 获取销售订单列表
   */
  @LogAnnotation
  @PostMapping(value = "/index", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData<Page<IndexOrdersResponse>> index(
      @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
      @RequestParam(value = "size", required = false, defaultValue = "15") Integer size,
      @RequestBody IndexOrdersRequest request
  ) {
    return ordersService.backendIndex(page, size, request);
  }

  /**
   * 获取销售订单详情
   */
  @LogAnnotation
  @GetMapping(value = "/detail/{no}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData detail(@PathVariable String no) {
    return ordersService.backendDetail(no);
  }

  /**
   * 整单优惠
   */
  @LogAnnotation
  @PutMapping(value = "/wholeDiscount/{no}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData wholeDiscount(@PathVariable String no, @RequestBody WholeDiscountRequest request) {
    return ordersService.wholeDiscount(no, request);
  }

  /**
   * 确认收银
   */
  @LogAnnotation
  @PostMapping(value = "/confirmPayment/{no}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData confirmPayment(@PathVariable String no, @RequestBody ConfirmPaymentRequest request) {
    return ordersService.confirmPayment(no, request);
  }

  /**
   * 备注
   */
  @LogAnnotation
  @PutMapping(value = "/editNotes/{no}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData editNotes(@PathVariable String no, @RequestBody EditNotesRequest request) {
    return ordersService.editNotes(no, request);
  }

  /**
   * 取消订单
   */
  @LogAnnotation
  @PutMapping(value = "/cancelOrder/{no}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData cancelOrder(@PathVariable String no) {
    return ordersService.cancelOrder(no);
  }

  /**
   * 确认完成
   */
  @LogAnnotation
  @PutMapping(value = "/confirmComplete/{no}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData confirmComplete(@PathVariable String no) {

    ResponseData responseData = ordersService.confirmComplete(no);

    if (responseData.getCode().equals(ResponseBackCode.SUCCESS.getValue())) {
      Orders orders = ordersService.selectByNo(no);
      pointService.resumePointByReturnOrder(orders.getId());
    }

    return responseData;
  }

  /**
   * 得到支付交易码
   */
  @LogAnnotation
  @GetMapping(value = "/getPaymentCode/{no}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseData getPaymentCode(@PathVariable String no) {
    return ordersService.getPaymentCode(no);
  }
}

