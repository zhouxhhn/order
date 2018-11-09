package cn.sipin.cloud.order.service.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;

import java.math.BigDecimal;

import cn.sipin.sales.cloud.order.pojo.Payment;
import cn.sipin.sales.cloud.order.request.payment.backend.IndexSalesPaymentRequest;
import cn.sipin.sales.cloud.order.request.payment.index.IndexPaymentRequest;
import cn.sipin.sales.cloud.order.request.payment.sum.SumPaymentRequest;
import cn.sipin.sales.cloud.order.response.backend.index.IndexSalesPaymentResponse;
import cn.sipin.sales.cloud.order.response.front.index.IndexPaymentResponse;
import cn.sipin.sales.cloud.order.response.front.sum.SumPaymentResponse;
import cn.siyue.platform.base.ResponseData;

/**
 * <p>
 * 支付流水信息表 服务类
 * </p>
 *
 */
public interface PaymentServiceContract extends IService<Payment> {

  /**
   * 前台流水
   */
  ResponseData<Page<IndexPaymentResponse>> index(int page,int size,IndexPaymentRequest request);

  /**
   * 汇总
   */
  ResponseData<SumPaymentResponse> sum(SumPaymentRequest request);

  /**
   * 退款更新支付表
   * @param returnType 退款方式
   * @param amount 退货金额
   * @param orderId 订单id
   */
  boolean returnPayment(String returnType, BigDecimal amount,Long orderId);


  /**
   * 后台流水
   */
  ResponseData<Page<IndexSalesPaymentResponse>> backIndex(Integer page, Integer size, IndexSalesPaymentRequest request,boolean flag);
}
