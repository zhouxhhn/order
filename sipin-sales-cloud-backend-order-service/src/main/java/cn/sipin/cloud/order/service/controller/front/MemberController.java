/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.controller.front;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import cn.sipin.cloud.order.service.contract.Loggable;
import cn.sipin.cloud.order.service.feign.impl.MaterialServiceImpl;
import cn.sipin.cloud.order.service.feign.impl.SalesUserServiceImpl;
import cn.sipin.cloud.order.service.sipin.client.SipinClient;
import cn.sipin.cloud.order.service.sipin.request.MemberApiRequest;
import cn.sipin.cloud.order.service.sipin.response.MemberApiResponse;
import cn.sipin.cloud.order.service.sipin.vo.coupon.CouponApiTask;
import cn.sipin.cloud.order.service.sipin.vo.member.MemberApiCoupon;
import cn.sipin.sales.cloud.order.response.AgencyCodeResponse;
import cn.sipin.sales.cloud.order.response.MaterialResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import cn.siyue.platform.exceptions.exception.RequestException;
import cn.siyue.platform.httplog.annotation.LogAnnotation;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/front/member")
public class MemberController implements Loggable {

  private SipinClient sipinClient;

  private MaterialServiceImpl materialService;

  private SalesUserServiceImpl salesUserService;

  @Autowired
  public MemberController(SipinClient sipinClient, MaterialServiceImpl materialService, SalesUserServiceImpl salesUserService) {
    this.sipinClient = sipinClient;
    this.materialService = materialService;
    this.salesUserService = salesUserService;
  }

  @LogAnnotation
  @ApiOperation(nickname = "frontGetMemberInfo", value = "获取会员消息")
  @GetMapping("/info/{mobile}")
  public ResponseData getMember(@PathVariable(value = "mobile") String mobile) {

      // 经销商信息-异步
      Future<ResponseData> future = null;
      try {
        // 异步操作
        future = salesUserService.getUserFutureByToken();
      } catch (RejectedExecutionException e) {
        logger().warn("异常信息：RejectedExecutionException" + e.getMessage());
        return new ResponseData(ResponseBackCode.ERROR_FAIL.getValue(), "获取经销商信息失败");
      }

      MemberApiRequest request = new MemberApiRequest(mobile);
      MemberApiResponse response = sipinClient.execute(request);

      // 异步获取经销商信息
      AgencyCodeResponse agencyInfoVo = salesUserService.getAgencyCodeResponseByFuture(future);

      if (response.getCode().equals(ResponseBackCode.SUCCESS.getValue())) {
        int size = response.getData().getCoupon().length;
        if (size > 0) {

          List<String> skuSnList = new ArrayList<>();

          MemberApiCoupon[] coupons = response.getData().getCoupon();
          List<MemberApiCoupon> list = new ArrayList<>(5);

          for (int i = 0; i < size; i++) {
            CouponApiTask tasks = coupons[i].getTask();

            if(StringUtils.isBlank(agencyInfoVo.getShopSourceId()) || (!StringUtils.isNumeric(agencyInfoVo.getShopSourceId()))) {
              // 无法获取门店sourceId 就无法显示优惠券
              continue;
            }

            if (search(tasks.getLimit(), Integer.valueOf(agencyInfoVo.getShopSourceId())) == null) {
              // 搜索不到 置零 只显示该门店的优惠券
              continue;
            }

            list.add(coupons[i]);

            if (tasks.getGoodsSkuSn().length > 0) {
              skuSnList.addAll(Arrays.asList(tasks.getGoodsSkuSn()));
            }
          }

          // 只显示门店优惠券
          response.getData().setCoupon(list.toArray(new MemberApiCoupon[list.size()]));

          if (skuSnList.size() > 0) {

            WeakHashMap<String, MaterialResponse> skusHashMap = null;

            skusHashMap = materialService.getSkuSnAndNoByRedis(skuSnList);

            if (Objects.isNull(skusHashMap)) {
              // 如果从redis中找不到
              skusHashMap = materialService.getSkusBySkuSns(skuSnList);
            }

            for (MemberApiCoupon coupon : response.getData().getCoupon()) {
              List<String> skuNos = new ArrayList<>(coupon.getTask().getGoodsSkuSn().length);
              for (String skuSn : coupon.getTask().getGoodsSkuSn()) {
                MaterialResponse materialResponse = skusHashMap.get(skuSn);
                if (!Objects.isNull(materialResponse)) {
                  skuNos.add(materialResponse.getSkuNo());
                }
              }

              coupon.getTask().setGoodsSkuNo(skuNos.toArray(new String[0]));
            }
          }
        }
      }

      return ResponseData.build(response.getCode(), response.getMsg(), response.getData());
  }

  private static Integer search(Integer[] arrs, Integer key) {
    for (Integer arr : arrs) {
      if (arr.equals(key)) {
        return arr;
      }
    }

    return null;
  }
}
