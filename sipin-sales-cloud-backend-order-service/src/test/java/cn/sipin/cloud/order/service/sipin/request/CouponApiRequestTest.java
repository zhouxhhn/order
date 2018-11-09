/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.sipin.request;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import cn.sipin.cloud.order.service.sipin.client.SipinClient;
import cn.sipin.cloud.order.service.sipin.response.CommonApiResponse;
import cn.sipin.cloud.order.service.sipin.response.CouponApiResponse;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("develop")
public class CouponApiRequestTest {


  @Autowired
  private SipinClient sipinClient;

  /**
   *  绑定优惠券
   */
  @Test
  public void testBind() {

    BindCouponApiRequest  req = new BindCouponApiRequest( "AFXRNWHWR","13800138001");
    CommonApiResponse resp = sipinClient.execute(req);
    assertEquals(Integer.valueOf(0), resp.getCode());

  }

  /**
   * 核销优惠券
   */
  @Test
  public void testConsume() {
    ConsumeCouponApiRequest req = new ConsumeCouponApiRequest("A0MQAZT21");
    CommonApiResponse resp = sipinClient.execute(req);
    assertEquals(Integer.valueOf(0), resp.getCode());

  }

  /**
   * 查询优惠券
   */
  @Test
  public void testSelect() {
     CouponApiRequest req = new CouponApiRequest("AG3SZBTSZ");
    CouponApiResponse resp = sipinClient.execute(req);
    assertEquals(Integer.valueOf(0), resp.getCode());

  }




}
