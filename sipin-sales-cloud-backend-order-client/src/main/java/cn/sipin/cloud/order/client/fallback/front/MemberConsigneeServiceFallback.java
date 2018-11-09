/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.fallback.front;

import org.springframework.stereotype.Component;

import cn.sipin.cloud.order.client.service.front.MemberConsigneeService;
import cn.sipin.sales.cloud.order.request.MemberAddressRequest;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

@Component
public class MemberConsigneeServiceFallback implements MemberConsigneeService {

  @Override public ResponseData getRegion() {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData createConsignee(String mobile, MemberAddressRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }

  @Override public ResponseData updateConsignee(String mobile, String addressId, MemberAddressRequest request) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }
}
