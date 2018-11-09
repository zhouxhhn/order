/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.feign.service;

import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.sipin.cloud.order.service.feign.callback.SalesUserServiceFallBack;
import cn.sipin.cloud.order.service.feign.request.IndexAgencyRequest;
import cn.sipin.sales.cloud.order.request.payment.index.IndexUserRequest;
import cn.sipin.sales.cloud.order.response.IndexSalesAgencyResponse;
import cn.sipin.sales.cloud.order.response.backend.index.SalesAgencyIndexResponse;
import cn.siyue.platform.base.ResponseData;

/**
 * 调用会员管理服务生产者的接口
 */
@FeignClient(name = "sales-member-service", fallback = SalesUserServiceFallBack.class)
public interface SalesUserService {


  @RequestMapping(value = "/sales/agency/indexAgency", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData<Page<SalesAgencyIndexResponse>> indexAgency(@RequestParam("page") int page, @RequestParam("size") int size, IndexAgencyRequest indexAgencyRequest);


  @RequestMapping(value = "/sales/agency/index", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData<Page<IndexSalesAgencyResponse>> getAllAgency();

  @RequestMapping(value = "/sales/user/indexUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData indexUser(@RequestParam("page") int page,
      @RequestParam("size") int size, IndexUserRequest request
  );

  @RequestMapping(value = "/sales/user/searchUser", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData searchUser(@RequestHeader("token") String token, @RequestParam("code") String code);

  @RequestMapping(value = "/sales/user/getUserByToken", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData getUserByToken();

  @RequestMapping(value = "/sales/user/getUserByToken", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData getUserByToken(@RequestHeader("token") String token);

  @RequestMapping(value = "/sales/agency/indexAgency", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData getAgencyInfos(@RequestParam("page") int page, @RequestParam("size") int size, IndexAgencyRequest indexAgencyRequest);

  @RequestMapping(value = "/sales/user/index", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData getUserInfoList();

}
