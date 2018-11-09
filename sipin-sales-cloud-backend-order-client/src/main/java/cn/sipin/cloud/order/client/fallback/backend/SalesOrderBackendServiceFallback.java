/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.fallback.backend;

import org.springframework.stereotype.Component;
import cn.sipin.cloud.order.client.service.backend.SalesOrderBackendService;
import cn.sipin.sales.cloud.order.request.backend.index.IndexOrdersRequest;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

@Component
public class SalesOrderBackendServiceFallback implements SalesOrderBackendService {

  @Override
  public ResponseData backendIndex(int page, int size,IndexOrdersRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override
  public ResponseData detail(String no) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData exportExcel(IndexOrdersRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

}
