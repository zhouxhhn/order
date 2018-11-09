/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.fallback.front;

import org.springframework.stereotype.Component;

import cn.sipin.cloud.order.client.service.front.MemberService;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;

@Component
public class MemberServiceFallback implements MemberService {

  @Override public ResponseData getMember(String mobile) {
    return ResponseData.build(
        ResponseBackCode.ERROR_DOWNGRADE.getValue(),
        ResponseBackCode.ERROR_DOWNGRADE.getMessage()
    );
  }
}
