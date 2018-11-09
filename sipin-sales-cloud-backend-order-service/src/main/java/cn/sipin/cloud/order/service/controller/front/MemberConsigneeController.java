/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.controller.front;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.sipin.cloud.order.service.sipin.client.SipinClient;
import cn.sipin.cloud.order.service.sipin.request.ConsigneeApiRequest;
import cn.sipin.cloud.order.service.sipin.request.RegionApiRequest;
import cn.sipin.cloud.order.service.sipin.response.CommonApiResponse;
import cn.sipin.cloud.order.service.sipin.response.RegionApiResponse;
import cn.sipin.cloud.order.service.sipin.vo.member.MemberConsigneeApiData;
import cn.sipin.sales.cloud.order.request.MemberAddressRequest;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import cn.siyue.platform.httplog.annotation.LogAnnotation;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/front/member-consignee")
public class MemberConsigneeController {

  private SipinClient sipinClient;

  @Autowired
  public MemberConsigneeController(SipinClient sipinClient) {

    this.sipinClient = sipinClient;
  }

  @LogAnnotation
  @ApiOperation(nickname = "frontGetRegion", value = "销售订单获取地区表")
  @GetMapping("/region")
  public ResponseData getRegion() {

    RegionApiRequest request = new RegionApiRequest();

    RegionApiResponse response = sipinClient.execute(request);

    return ResponseData.build(response.getCode(), response.getMsg(), response.getData());
  }

  @LogAnnotation
  @ApiOperation(nickname = "frontCreateMemberConsignee", value = "创建会员地址")
  @PostMapping("/create/{mobile}/address")
  public ResponseData createConsignee(
      @PathVariable(value = "mobile") String mobile,
      @RequestBody MemberAddressRequest request
  ) {
    MemberConsigneeApiData apiAddress = new MemberConsigneeApiData();
    BeanUtils.copyProperties(request, apiAddress);
    ConsigneeApiRequest consigneeApiRequest = new ConsigneeApiRequest(mobile, apiAddress);

    CommonApiResponse response = sipinClient.execute(consigneeApiRequest);

    return ResponseData.build(response.getCode(), response.getMsg());
  }

  @LogAnnotation
  @ApiOperation(nickname = "frontUpdateMemberConsignee", value = "更新会员地址")
  @PutMapping("/update/{mobile}/address/{addressId}")
  public ResponseData updateConsignee(
      @PathVariable(value = "mobile") String mobile,
      @PathVariable(value = "addressId") String addressId,
      @RequestBody MemberAddressRequest request
  ) {
    MemberConsigneeApiData apiAddress = new MemberConsigneeApiData();
    BeanUtils.copyProperties(request, apiAddress);
    ConsigneeApiRequest consigneeApiRequest = new ConsigneeApiRequest(mobile, addressId, apiAddress);

    CommonApiResponse response = sipinClient.execute(consigneeApiRequest);

    return ResponseData.build(response.getCode(), response.getMsg());
  }



}
