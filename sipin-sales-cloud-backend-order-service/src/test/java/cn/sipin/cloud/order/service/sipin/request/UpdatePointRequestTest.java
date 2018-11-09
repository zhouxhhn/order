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
import cn.sipin.cloud.order.service.sipin.response.RegionApiResponse;
import cn.sipin.cloud.order.service.sipin.response.UpdatePointApiResponse;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("develop")
public class UpdatePointRequestTest {

  @Autowired
  private SipinClient sipinClient;

  private UpdatePointApiRequest req;

  @Before
  public void setUp() {

    req = new UpdatePointApiRequest("13800138001", 1, (byte) 24, "378383838");
  }

  @Test
  public void testExecution() {
    UpdatePointApiResponse resp = sipinClient.execute(req);
    assertEquals(Integer.valueOf(0), resp.getCode());
  }

}
