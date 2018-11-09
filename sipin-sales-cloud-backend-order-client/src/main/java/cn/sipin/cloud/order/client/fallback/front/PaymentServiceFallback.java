/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.fallback.front;

import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.stereotype.Component;

import cn.sipin.cloud.order.client.service.front.PaymentService;
import cn.sipin.cloud.order.client.service.front.SalesOrderService;
import cn.sipin.sales.cloud.order.request.SalesOrderRequest;
import cn.sipin.sales.cloud.order.request.backend.index.IndexOrdersRequest;
import cn.sipin.sales.cloud.order.request.confirmPayment.ConfirmPaymentRequest;
import cn.sipin.sales.cloud.order.request.editNotes.EditNotesRequest;
import cn.sipin.sales.cloud.order.request.payment.index.IndexPaymentRequest;
import cn.sipin.sales.cloud.order.request.payment.sum.SumPaymentRequest;
import cn.sipin.sales.cloud.order.request.wholeDiscount.WholeDiscountRequest;
import cn.sipin.sales.cloud.order.response.SalesOrderResponse;
import cn.sipin.sales.cloud.order.response.front.index.IndexPaymentResponse;
import cn.sipin.sales.cloud.order.response.front.sum.SumPaymentResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

@Component
public class PaymentServiceFallback implements PaymentService {

  @Override
  public ResponseData<Page<IndexPaymentResponse>> index(int page, int size, IndexPaymentRequest request) {
    return new ResponseData<>(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override
  public ResponseData<SumPaymentResponse> sum(SumPaymentRequest request) {
    return new ResponseData<>(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }
}
