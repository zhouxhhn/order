/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.fallback.front;

import org.springframework.stereotype.Component;

import cn.sipin.cloud.order.client.service.front.SalesOrderService;
import cn.sipin.sales.cloud.order.request.SalesOrderRequest;
import cn.sipin.sales.cloud.order.request.backend.index.IndexOrdersRequest;
import cn.sipin.sales.cloud.order.request.confirmPayment.ConfirmPaymentRequest;
import cn.sipin.sales.cloud.order.request.editNotes.EditNotesRequest;
import cn.sipin.sales.cloud.order.request.wholeDiscount.WholeDiscountRequest;
import cn.sipin.sales.cloud.order.response.SalesOrderResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

@Component
public class SalesOrderServiceFallback implements SalesOrderService {

  @Override public ResponseData<SalesOrderResponse> store(SalesOrderRequest salesOrderRequest) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData index(int page, int size, IndexOrdersRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData detail(String no) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData wholeDiscount(String no, WholeDiscountRequest wholeDiscountRequest) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData confirmPayment(String no, ConfirmPaymentRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData editNotes(String no, EditNotesRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData cancelOrder(String no) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData confirmComplete(String no) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData getPaymentCode(String no) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }
}
