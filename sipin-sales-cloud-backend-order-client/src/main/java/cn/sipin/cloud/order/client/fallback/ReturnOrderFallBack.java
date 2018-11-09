package cn.sipin.cloud.order.client.fallback;

import cn.sipin.cloud.order.client.service.ReturnOrderClient;
import cn.sipin.sales.cloud.order.common.PageResponse;
import cn.sipin.sales.cloud.order.request.returnOrder.ReturnOrderSearchRequest;
import cn.sipin.sales.cloud.order.response.returnOrder.ReturnOrderVo;
import cn.siyue.platform.base.ResponseData;
import org.springframework.stereotype.Component;

@Component
public class ReturnOrderFallBack extends BaseServiceFallBack implements ReturnOrderClient {

    @Override
    public ResponseData<PageResponse<ReturnOrderVo>> search(ReturnOrderSearchRequest requestParam, String sign) {
        return getDownGradeResponse();
    }


}
