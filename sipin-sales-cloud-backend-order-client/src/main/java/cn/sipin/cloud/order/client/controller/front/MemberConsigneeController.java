/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.controller.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.sipin.cloud.order.client.service.front.MemberConsigneeService;
import cn.sipin.sales.cloud.order.request.MemberAddressRequest;
import cn.siyue.platform.base.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/front/member-consignee")
@Api(tags = "经销商_斯品会员地址管理_前台API")
public class MemberConsigneeController {

  private MemberConsigneeService memberConsigneeService;

  @Autowired
  public MemberConsigneeController(MemberConsigneeService memberConsigneeService) {
    this.memberConsigneeService = memberConsigneeService;
  }

  @ApiOperation(nickname = "frontGetRegion", value = "销售订单获取地区表")
  @GetMapping("/region")
  public ResponseData getRegion() {

    return memberConsigneeService.getRegion();
  }

  @ApiOperation(nickname = "frontCreateMemberConsignee", value = "创建会员地址")
  @PostMapping("/create/{mobile}/address")
  public ResponseData createConsignee(
      @PathVariable(value = "mobile") String mobile,
      @RequestBody MemberAddressRequest request
  ) {

    return memberConsigneeService.createConsignee(mobile, request);
  }

  @ApiOperation(nickname = "frontUpdateMemberConsignee", value = "更新会员地址")
  @PutMapping("/update/{mobile}/address/{addressId}")
  public ResponseData updateConsignee(
      @PathVariable(value = "mobile") String mobile,
      @PathVariable(value = "addressId") String addressId,
      @RequestBody MemberAddressRequest request
  ) {

    return memberConsigneeService.updateConsignee(mobile, addressId, request);
  }
}
