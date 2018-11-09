/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.fallback;

import org.springframework.stereotype.Component;

import cn.sipin.cloud.order.client.service.ErpService;
import cn.sipin.sales.cloud.order.request.erp.ErpOrderExpressRequest;
import cn.sipin.sales.cloud.order.request.erp.ErpOrdersRequest;
import cn.sipin.sales.cloud.order.request.erp.ErpUpdateOrderStatusRequest;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

@Component
public class ErpServiceFallBack implements ErpService {

  @Override public ResponseData index(String sign, ErpOrdersRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData updateOrderStatus(String sign, ErpUpdateOrderStatusRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData createExpress(String sign, ErpOrderExpressRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }
}
