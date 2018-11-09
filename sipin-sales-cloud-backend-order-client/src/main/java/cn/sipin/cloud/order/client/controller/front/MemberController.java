/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.client.controller.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.sipin.cloud.order.client.service.front.MemberService;
import cn.siyue.platform.base.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/front/member")
@Api(tags = "经销商_斯品会员管理_前台API")
public class MemberController {

  private MemberService memberService;

  @Autowired
  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  @ApiOperation(nickname = "frontGetMemberInfo", value = "获取会员消息")
  @GetMapping("/info/{mobile}")
  public ResponseData getMember(@PathVariable(value = "mobile") String mobile) {

    return memberService.getMember(mobile);
  }
}
