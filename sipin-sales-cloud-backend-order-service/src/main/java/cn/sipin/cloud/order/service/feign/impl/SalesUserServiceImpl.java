/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.feign.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;

import cn.sipin.cloud.order.service.executor.UsersThreadPoolExecutor;
import cn.sipin.cloud.order.service.feign.request.IndexAgencyRequest;
import cn.sipin.cloud.order.service.feign.service.SalesUserService;
import cn.sipin.cloud.order.service.util.JsonTransformer;
import cn.sipin.sales.cloud.order.response.AgencyCodeResponse;
import cn.sipin.sales.cloud.order.response.IndexSalesAgencyResponse;
import cn.sipin.sales.cloud.order.response.backend.index.IndexUserResponse;
import cn.sipin.sales.cloud.order.response.backend.index.SalesAgencyIndexResponse;
import cn.sipin.sales.cloud.order.response.returnOrder.IndexReturnOrdersResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import cn.siyue.platform.exceptions.exception.RequestException;

@Service
public class SalesUserServiceImpl {

  private SalesUserService salesUserService;

  private UsersThreadPoolExecutor customThreadPoolExecutor;

  @Autowired
  public SalesUserServiceImpl(SalesUserService salesUserService, UsersThreadPoolExecutor customThreadPoolExecutor) {
    this.salesUserService = salesUserService;
    this.customThreadPoolExecutor = customThreadPoolExecutor;
  }

  public HashMap<String, String> getAllAgencyMap() {

    ResponseData<Page<IndexSalesAgencyResponse>> agencyResponse = salesUserService.getAllAgency();
    if (!agencyResponse.getCode().equals(ResponseBackCode.SUCCESS.getValue())) {
      return null;
    }

    Page<IndexSalesAgencyResponse> data = (Page<IndexSalesAgencyResponse>) agencyResponse.getData();

    List<IndexSalesAgencyResponse> responseList = data.getRecords();

    HashMap<String, String> outerCodeMap = new HashMap<>(responseList.size());

    responseList.forEach(it -> {
      if (StringUtils.isNotBlank(it.getOuterCode())) {
        outerCodeMap.put(it.getCode(), it.getOuterCode());
      }
    });

    return outerCodeMap;
  }


  /**
   * 返回含经销商门店名称及sourceId为value的HashMap
   *
   *     key => value
   *     agencyCode => agencyName
   *     shopCode => shopName
   *     agencyCode+shopCode => sourceId
   * @return
   */
  public HashMap<String, String> getAllAgencyAndShopNameAndSourceMap() {

    ResponseData<Page<SalesAgencyIndexResponse>> pageResponseData = salesUserService.indexAgency(1, 100, new IndexAgencyRequest());

    if (!pageResponseData.getCode().equals(ResponseBackCode.SUCCESS.getValue())) {
      return null;
    }

    Page<SalesAgencyIndexResponse> data = (Page<SalesAgencyIndexResponse>) pageResponseData.getData();
    List<SalesAgencyIndexResponse> responseList = data.getRecords();
    if (responseList.size() == 0) {
      return null;
    }

    HashMap<String, String> nameAndSourceMap = new HashMap<>(10);
    responseList.forEach(agency -> {
      // 经销商code -> 经销商名称
      nameAndSourceMap.put(agency.getCode(), agency.getName());

      if (agency.getShopResponseList().size() > 0) {
        agency.getShopResponseList().forEach(shop -> {
          // 门店code -> 门店名称
          nameAndSourceMap.put(shop.getCode(), shop.getName());

          nameAndSourceMap.put(agency.getCode() + shop.getCode(), shop.getSourceId());
        });
      }
    });

    return nameAndSourceMap;
  }

  public AgencyCodeResponse getUserByToken() {
    ResponseData agencyData = salesUserService.getUserByToken();
    if (!agencyData.getCode().equals(ResponseBackCode.SUCCESS.getValue())) {
      throw new RequestException(
          ResponseBackCode.FILE_NOT_FOUND.getValue(),
          "获取门店用户信息返回失败"
      );
    }

    AgencyCodeResponse agencyInfoVo = JsonTransformer
        .getObjectMapper()
        .convertValue(agencyData.getData(), new TypeReference<AgencyCodeResponse>() {});

    return agencyInfoVo;
  }

  public Future<ResponseData> getUserFutureByToken() {
    HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    String token = request.getHeader("token");

    Callable<ResponseData> userTask = () -> {
      return salesUserService.getUserByToken(token);
    };
//    Callable<ResponseData> userTask = new Callable<ResponseData>() {
//      @Override public ResponseData call() throws Exception {
//        return salesUserService.getUserByToken(token);
//      }
//    };

    ThreadPoolExecutor poolExecutor = customThreadPoolExecutor.getPool();
    return poolExecutor.submit(userTask);
  }

  /**
   * 该方法为阻塞方法
   */
  public AgencyCodeResponse getAgencyCodeResponseByFuture(Future<ResponseData> future) {
    try {
      // 5豪秒超时设置
      return parseAgencyResponseData(future.get(5, TimeUnit.MILLISECONDS));
    } catch (InterruptedException e) {
      e.printStackTrace();
      throw new RequestException(ResponseBackCode.UNKNOWN_ERROR.getValue(), "获取用户信息中断");
    } catch (ExecutionException e) {
      e.printStackTrace();
      throw new RequestException(ResponseBackCode.UNKNOWN_ERROR.getValue(), "获取用户信息异常：" + e.getMessage());
    } catch (TimeoutException e) {
      e.printStackTrace();
      throw new RequestException(ResponseBackCode.UNKNOWN_ERROR.getValue(), "获取用户信息超时");
    }
  }

  public AgencyCodeResponse parseAgencyResponseData(ResponseData agencyData) {

    if (!agencyData.getCode().equals(ResponseBackCode.SUCCESS.getValue())) {
      throw new RequestException(
          ResponseBackCode.FILE_NOT_FOUND.getValue(),
          "获取门店用户信息返回失败:"  + agencyData.getMsg()
      );
    }

    AgencyCodeResponse agencyInfoVo = JsonTransformer
        .getObjectMapper()
        .convertValue(agencyData.getData(), new TypeReference<AgencyCodeResponse>() {});

    return agencyInfoVo;
  }


  public Future<ResponseData> getAgencyInfos() {
    Callable<ResponseData> userTask = () -> {
      ResponseData responseData = salesUserService.getAgencyInfos(1,10000,new IndexAgencyRequest());
      return responseData;
    };
    ThreadPoolExecutor poolExecutor = customThreadPoolExecutor.getPool();
    return poolExecutor.submit(userTask);
  }

  public ResponseData getAgencyInfoList() {
    return salesUserService.getAgencyInfos(1,10000,new IndexAgencyRequest());
  }

  /**
   * 获取用户
   */
  public Map<Long,String> getUserInfoList() {
    ResponseData responseData =  salesUserService.getUserInfoList();
    List<IndexUserResponse> userResponseList = JsonTransformer
        .getObjectMapper()
        .convertValue(responseData.getData(), new TypeReference<List<IndexUserResponse>>() {});
    Map<Long,String> map = new HashMap<>();
    if(userResponseList != null && !userResponseList.isEmpty()){
      for (int i = 0,size = userResponseList.size(); i<size; i++){
        IndexUserResponse response = userResponseList.get(i);
        map.put(response.getId(),response.getName());
      }
    }
    return map;
  }

}
