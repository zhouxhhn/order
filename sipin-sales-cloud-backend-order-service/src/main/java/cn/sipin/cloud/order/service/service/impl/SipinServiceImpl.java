/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import cn.sipin.cloud.order.service.executor.UsersThreadPoolExecutor;
import cn.sipin.cloud.order.service.service.SipinServiceContract;
import cn.sipin.cloud.order.service.sipin.client.SipinClient;
import cn.sipin.cloud.order.service.sipin.request.ConsumeCouponApiRequest;
import cn.sipin.cloud.order.service.sipin.request.MemberApiRequest;
import cn.sipin.cloud.order.service.sipin.request.ResumeCouponApiRequest;
import cn.sipin.cloud.order.service.sipin.request.UpdatePointApiRequest;
import cn.sipin.cloud.order.service.sipin.response.CommonApiResponse;
import cn.sipin.cloud.order.service.sipin.response.MemberApiResponse;
import cn.sipin.cloud.order.service.sipin.response.UpdatePointApiResponse;
import cn.sipin.cloud.order.service.sipin.vo.member.MemberApi;
import cn.siyue.platform.constants.ResponseBackCode;
import cn.siyue.platform.exceptions.exception.RequestException;

@Service
public class SipinServiceImpl implements SipinServiceContract {

  private SipinClient sipinClient;

  private UsersThreadPoolExecutor customThreadPoolExecutor;

  @Autowired
  public SipinServiceImpl(SipinClient sipinClient, UsersThreadPoolExecutor customThreadPoolExecutor) {
    this.sipinClient = sipinClient;
    this.customThreadPoolExecutor = customThreadPoolExecutor;
  }

  @Override public MemberApi getMember(String mobile) {
    MemberApiRequest req = new MemberApiRequest(mobile);
    MemberApiResponse resp = sipinClient.execute(req);
    if (resp.getCode().equals(ResponseBackCode.SUCCESS.getValue())) {
      return resp.getData();
    }

    throw new RequestException(resp.getCode(), resp.getMsg());
  }

  //核销优惠券
  @Override
  public Future<Boolean> cancelCoupon(String code) {
    try {
      ConsumeCouponApiRequest req = new ConsumeCouponApiRequest(code);
      Callable<Boolean> userTask = () -> {
        CommonApiResponse resp = sipinClient.execute(req);
        return resp.getCode() == 0 ? true : false;
      };
      ThreadPoolExecutor poolExecutor = customThreadPoolExecutor.getPool();
      return poolExecutor.submit(userTask);
    } catch (Exception e) {
      return null;
    }
  }

  @Override public Future<Boolean> resumeCoupon(String code) {

    try {
      ResumeCouponApiRequest request = new ResumeCouponApiRequest(code);
      Callable<Boolean> couponTask = () -> {
        CommonApiResponse resp = sipinClient.execute(request);
        return resp.getCode() == 0 ? true : false;
      };
      ThreadPoolExecutor poolExecutor = customThreadPoolExecutor.getPool();
      return poolExecutor.submit(couponTask);
    } catch (Exception e) {
      e.printStackTrace();

      throw e;
    }
  }

  //更新积分
  @Override
  public Future<Boolean> updatePoint(String mobile, int point, byte typeId, String orderNo) {
    try {
      UpdatePointApiRequest req = new UpdatePointApiRequest(mobile, point, typeId, orderNo);
      Callable<Boolean> userTask = () -> {
        UpdatePointApiResponse resp = sipinClient.execute(req);
        return resp.getCode() == 0 ? true : false;
      };
      ThreadPoolExecutor poolExecutor = customThreadPoolExecutor.getPool();
      return poolExecutor.submit(userTask);
    } catch (Exception e) {
      return null;
    }
  }
}
