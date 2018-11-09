package cn.sipin.cloud.order.service.sipin.request;



import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.TreeMap;

import cn.sipin.cloud.order.service.sipin.client.SipinClient;
import cn.sipin.cloud.order.service.sipin.response.MemberApiResponse;
import cn.sipin.cloud.order.service.sipin.vo.member.MemberApi;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("develop")
public class MemberApiRequestTest  {

  @Autowired
  private SipinClient sipinClient;

  private MemberApiRequest req;

  @Before
  public void setUp() {

    req = new MemberApiRequest( "13800138001");
  }

  @Test
  public void testExecution() {
    MemberApiResponse resp = sipinClient.execute(req);
    assertEquals(Integer.valueOf(0), resp.getCode());
    MemberApi data = resp.getData();
    assertEquals("13800138001", data.getMobile());
    assertEquals(Double.valueOf(0.95), data.getLevel().getDiscountRate());
    assertEquals("黑金会员", data.getLevel().getName());
  }

}
