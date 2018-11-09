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
import cn.sipin.cloud.order.service.util.JsonTransformer;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("develop")
public class RegionApiRequestTest {

  @Autowired
  private SipinClient sipinClient;

  private RegionApiRequest req;

  @Before
  public void setUp() {

    req = new RegionApiRequest();
  }

  @Test
  public void testExecution() {
    RegionApiResponse resp = sipinClient.execute(req);
    assertEquals(Integer.valueOf(0), resp.getCode());
   }
}
