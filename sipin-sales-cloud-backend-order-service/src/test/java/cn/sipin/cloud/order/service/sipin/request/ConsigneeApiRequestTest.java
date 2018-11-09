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
import cn.sipin.cloud.order.service.sipin.vo.member.MemberConsigneeApiData;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("develop")
public class ConsigneeApiRequestTest {

  @Autowired
  private SipinClient sipinClient;

  private ConsigneeApiRequest req;


  @Test
  public void createConsignee() {

    MemberConsigneeApiData data = new MemberConsigneeApiData();
    data.setProvinceId(152);
    data.setProvinceName("北京市");
    data.setCityId(8);
    data.setCityName("朝阳区");
    data.setDistrictId(1);
    data.setDistrictName("三环以内");
    data.setAddress("超级豪宅1栋");
    data.setConsignee("张数");
    data.setMobile("13800138000");
    data.setIsDefault((byte)0);
    ConsigneeApiRequest consigneeApiRequest = new ConsigneeApiRequest("13800138000", data);

   CommonApiResponse response =  sipinClient.execute(consigneeApiRequest);

    assertEquals(Integer.valueOf(0), response.getCode());
  }


  @Test
  public void updateConsignee() {

    MemberConsigneeApiData data = new MemberConsigneeApiData();
    data.setProvinceId(152);
    data.setProvinceName("北京市");
    data.setCityId(8);
    data.setCityName("朝阳区");
    data.setDistrictId(1);
    data.setDistrictName("三环以内");
    data.setAddress("超级豪宅2331栋");
    data.setConsignee("张数");
    data.setMobile("13800138000");
    data.setIsDefault((byte)0);
    ConsigneeApiRequest consigneeApiRequest = new ConsigneeApiRequest("13800138000",String.valueOf(60), data);

    CommonApiResponse response =  sipinClient.execute(consigneeApiRequest);

    assertEquals(Integer.valueOf(0), response.getCode());
  }

}
