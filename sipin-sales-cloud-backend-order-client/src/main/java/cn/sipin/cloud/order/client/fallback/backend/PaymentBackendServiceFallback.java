/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.fallback.backend;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Component;
import cn.sipin.cloud.order.client.service.backend.PaymentBackendService;
import cn.sipin.sales.cloud.order.request.payment.backend.IndexSalesPaymentRequest;
import cn.sipin.sales.cloud.order.response.backend.index.IndexSalesPaymentResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

@Component
public class PaymentBackendServiceFallback implements PaymentBackendService {

  @Override
  public ResponseData<Page<IndexSalesPaymentResponse>> index(int page, int size, IndexSalesPaymentRequest request) {
    return new ResponseData<>(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData exportExcel(IndexSalesPaymentRequest request) {
    return new ResponseData<>( ResponseBackCode.ERROR_DOWNGRADE.getValue(), ResponseBackCode.ERROR_DOWNGRADE.getMessage() );
  }
}
