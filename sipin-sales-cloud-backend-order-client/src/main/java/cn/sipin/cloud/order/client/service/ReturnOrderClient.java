package cn.sipin.cloud.order.client.service;

import cn.sipin.cloud.order.client.fallback.ReturnOrderFallBack;
import cn.sipin.sales.cloud.order.common.PageResponse;
import cn.sipin.sales.cloud.order.request.returnOrder.ReturnOrderSearchRequest;
import cn.sipin.sales.cloud.order.response.returnOrder.ReturnOrderVo;
import cn.siyue.platform.base.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service", fallback = ReturnOrderFallBack.class)
public interface ReturnOrderClient {

    @RequestMapping(value = "/returnOrderForErp/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseData<PageResponse<ReturnOrderVo>> search(ReturnOrderSearchRequest requestParam, @RequestParam(value = "sign") String sign);


}
