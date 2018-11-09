package cn.sipin.cloud.order.service.controller;

import cn.sipin.cloud.order.service.config.ErpConfig;
import cn.sipin.cloud.order.service.service.ReturnOrderServiceContract;
import cn.sipin.cloud.order.service.util.SignUtil;
import cn.sipin.sales.cloud.order.request.returnOrder.ReturnOrderSearchRequest;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import cn.siyue.platform.exceptions.exception.RequestException;
import cn.siyue.platform.httplog.annotation.LogAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api(tags = "销售退货单管理接口")
@RestController
@RequestMapping("/returnOrderForErp")
public class ReturnOrderForErpController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReturnOrderForErpController.class);

    @Autowired
    private ReturnOrderServiceContract returnOrderService;

    @Autowired
    private ErpConfig erpConfig;

    @ApiOperation(nickname = "search",value = "搜索销售退货单接口")
    @LogAnnotation
    @PostMapping("/search")
    public ResponseData search(@RequestBody ReturnOrderSearchRequest requestParam, @RequestParam(value = "sign") String sign, BindingResult result) {
        //请求的数据参数格式不正确
        if (result.hasErrors()) {
            return getErrorResponse(result);
        }

        // 验证x-erp-token
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        if (!erpConfig.getXErpToken().equals(httpServletRequest.getHeader("X-Erp-Token"))) {
            throw new RequestException(ResponseBackCode.ERROR_TOKEN_TIMEOUT_CODE.getValue(), ResponseBackCode.ERROR_TOKEN_TIMEOUT_CODE.getMessage());
        }

        // 验证签名
        String rawSign = SignUtil.sign(requestParam, erpConfig.getSecret());
        if (!rawSign.equals(sign)) {
            throw new RequestException(ResponseBackCode.ERROR_AUTH_FAIL.getValue(), ResponseBackCode.ERROR_AUTH_FAIL.getMessage() + "：sign验证失败");
        }

        return returnOrderService.search(requestParam);
    }



}
