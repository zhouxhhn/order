package cn.sipin.cloud.order.service.sipin.request;

import cn.sipin.cloud.order.service.util.SignUtil;
import cn.sipin.sales.cloud.order.request.returnOrder.ReturnOrderSearchRequest;
import org.junit.Test;

public class SignTest {

    @Test
    public void testSign() {
        String secret = System.getenv("secret");
        ReturnOrderSearchRequest requestParam = new ReturnOrderSearchRequest();
        requestParam.setPage(1);
        requestParam.setSize(3);
        System.out.println("signData:" + SignUtil.sign(requestParam, secret));

    }
}
