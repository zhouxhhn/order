package cn.sipin.cloud.order.service.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import cn.sipin.sales.cloud.order.pojo.Orders;
import cn.sipin.sales.cloud.order.pojo.Payment;
import cn.sipin.sales.cloud.order.request.erp.ErpOrdersRequest;
import cn.sipin.sales.cloud.order.request.payment.backend.IndexSalesPaymentRequest;
import cn.sipin.sales.cloud.order.request.payment.index.IndexPaymentRequest;
import cn.sipin.sales.cloud.order.request.payment.sum.SumPaymentRequest;

/**
 * <p>
 * 支付流水信息表 Mapper 接口
 * </p>
 *
 */
public interface PaymentMapper extends BaseMapper<Payment> {

  List<Payment> index(@Param("limit") Integer limit, @Param("offset") Integer offset,@Param("shopCode") String shopCode, @Param("request") IndexPaymentRequest request);

  List<Payment> sumPayment(@Param("limit") Integer limit, @Param("offset") Integer offset,
                           @Param("receive") Integer receive,
                           @Param("back") Integer back,
                           @Param("shopCode") String shopCode,
                           @Param("request") SumPaymentRequest request);

  /** 后台查询销售资金流水*/
  List<Payment> backendIndex(@Param("limit") Integer limit, @Param("offset") Integer offset,@Param("exchangeType") Integer exchangeType, @Param("request") IndexSalesPaymentRequest
      request);


}
