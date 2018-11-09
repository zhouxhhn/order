package cn.sipin.cloud.order.service.sipin.request;

import cn.sipin.cloud.order.service.OrderServerApplication;
import cn.sipin.cloud.order.service.service.ReturnOrderServiceContract;
import cn.sipin.cloud.order.service.util.SignUtil;
import cn.sipin.sales.cloud.order.common.PageResponse;
import cn.sipin.sales.cloud.order.request.returnOrder.ReturnOrderSearchRequest;
import cn.sipin.sales.cloud.order.response.returnOrder.ReturnOrderVo;
import cn.siyue.platform.base.ResponseData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReturnOrderServiceTest {

    @Autowired
    private ReturnOrderServiceContract returnOrderServiceContract;

    @Test
    public void testSearch() {
        ReturnOrderSearchRequest requestParam = new ReturnOrderSearchRequest();
        requestParam.setPage(1);
        requestParam.setSize(3);
        ResponseData<PageResponse<ReturnOrderVo>> respData = returnOrderServiceContract.search(requestParam);
        System.out.println("respData");
    }


}
