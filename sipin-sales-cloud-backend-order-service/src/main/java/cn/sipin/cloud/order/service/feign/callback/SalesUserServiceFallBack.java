/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.feign.callback;


import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.stereotype.Component;

import cn.sipin.cloud.order.service.feign.request.IndexAgencyRequest;
import cn.sipin.cloud.order.service.feign.service.SalesUserService;
import cn.sipin.sales.cloud.order.request.payment.index.IndexUserRequest;
import cn.sipin.sales.cloud.order.response.IndexSalesAgencyResponse;
import cn.sipin.sales.cloud.order.response.backend.index.SalesAgencyIndexResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

/**
 * 门店管理员熔断器
 */
@Component
public class SalesUserServiceFallBack implements SalesUserService {

  @Override public ResponseData<Page<SalesAgencyIndexResponse>> indexAgency(
      int page, int size, IndexAgencyRequest indexAgencyRequest
  ) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData<Page<IndexSalesAgencyResponse>> getAllAgency() {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData indexUser(int page, int size, IndexUserRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData searchUser(String token, String code) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }


  @Override public ResponseData getUserByToken() {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData getUserByToken(String token) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData getAgencyInfos(int page, int size,IndexAgencyRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData getUserInfoList() {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }
}
