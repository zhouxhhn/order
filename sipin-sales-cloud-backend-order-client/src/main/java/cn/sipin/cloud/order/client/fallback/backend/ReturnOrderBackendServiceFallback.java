/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.fallback.backend;

import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.stereotype.Component;

import cn.sipin.cloud.order.client.service.backend.ReturnOrderBackendService;
import cn.sipin.sales.cloud.order.request.returnOrder.IndexReturnOrdersRequest;
import cn.sipin.sales.cloud.order.response.returnOrder.IndexReturnOrdersResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

@Component
public class ReturnOrderBackendServiceFallback implements ReturnOrderBackendService {

  @Override public ResponseData<Page<IndexReturnOrdersResponse>> index(
      Integer page, Integer size, IndexReturnOrdersRequest request
  ) {
    return new ResponseData<>(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData<IndexReturnOrdersResponse> show(String orderNo) {
    return new ResponseData<>(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData exportExcel(IndexReturnOrdersRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }
}
