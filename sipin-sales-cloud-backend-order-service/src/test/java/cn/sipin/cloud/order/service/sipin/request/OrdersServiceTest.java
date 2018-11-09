/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.sipin.request;

import com.baomidou.mybatisplus.plugins.Page;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import cn.sipin.cloud.order.service.service.OrdersServiceContract;
import cn.sipin.sales.cloud.order.request.erp.ErpOrdersRequest;
import cn.sipin.sales.cloud.order.response.ErpSalesOrderResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("develop")
public class OrdersServiceTest {

  @Autowired
  private OrdersServiceContract ordersService;

  @Test
  public void getErpPageTest() {

    ErpOrdersRequest request = new ErpOrdersRequest();
    request.setBeginDate("2018-08-15 17:37:07");
    request.setEndDate("2018-08-17 17:37:07");

    request.setPage(1);
    request.setPageSize(20);

    Page<ErpSalesOrderResponse> orderVoPage = new Page<>(request.getPage(), request.getPageSize());
    orderVoPage.setAsc(false);

    orderVoPage = ordersService.selectOrderForErpPage(orderVoPage, request);

    Assert.assertTrue(orderVoPage.getSize() > 0);
  }
}
