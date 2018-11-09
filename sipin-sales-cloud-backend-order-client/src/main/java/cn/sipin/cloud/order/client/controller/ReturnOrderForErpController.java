package cn.sipin.cloud.order.client.controller;


import cn.sipin.cloud.order.client.service.ReturnOrderClient;
import cn.sipin.sales.cloud.order.common.PageResponse;
import cn.sipin.sales.cloud.order.request.returnOrder.ReturnOrderSearchRequest;
import cn.sipin.sales.cloud.order.response.returnOrder.ReturnOrderVo;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.httplog.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "ERP获取销售退货单管理接口")
@RestController
@RequestMapping("/erp/sales-orders/return-order")
public class ReturnOrderForErpController extends BaseController {

    @Autowired
    private ReturnOrderClient returnOrderClient;


    @ApiOperation(nickname = "erpReturnOrderSearch",value = "搜索销售退货单接口")
    @LogAnnotation
    @PostMapping("/search")
    public ResponseData<PageResponse<ReturnOrderVo>> search(
        @RequestBody ReturnOrderSearchRequest requestParam,
        @RequestParam(value = "sign") String sign,
        BindingResult result) {
        //请求的数据参数格式不正确
        if (result.hasErrors()) {
            return getErrorResponse(result);
        }
        return returnOrderClient.search(requestParam, sign);
    }

}
