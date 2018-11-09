/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.feign.impl;

import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.sipin.cloud.order.service.feign.service.MaterialService;
import cn.sipin.cloud.order.service.service.RedisClusterServiceContract;
import cn.sipin.cloud.order.service.util.JsonTransformer;
import cn.sipin.sales.cloud.order.pojo.OrderDetail;
import cn.sipin.sales.cloud.order.request.vo.OrderSkuDetailVo;
import cn.sipin.sales.cloud.order.response.MaterialResponse;
import cn.siyue.platform.base.ResponseData;
import cn.siyue.platform.constants.ResponseBackCode;
import cn.siyue.platform.exceptions.exception.RequestException;

@Service
public class MaterialServiceImpl {

  private final static Logger logger = LoggerFactory.getLogger(MaterialServiceImpl.class);

  public final static String REDIS_SKU_SN_NO_HASH = "sku_sn_no_hash";

  public final static String REDIS_SKU_NO_MATERIAL_HASH = "sku_no_material_entity_hash";

  private MaterialService materialService;

  private RedisClusterServiceContract redisClusterService;

  @Autowired
  public MaterialServiceImpl(MaterialService materialService, RedisClusterServiceContract redisClusterService) {
    this.materialService = materialService;
    this.redisClusterService = redisClusterService;
  }

  public WeakHashMap<String, MaterialResponse> getSkuSnAndNoByRedis(List<String> skuSnList) {

    if (!redisClusterService.existKey(REDIS_SKU_SN_NO_HASH)) {
      return null;
    }

    // 如果 skuSn值不存在Redis散列表field中，则会返回空值
    List<Object> skuNoList = redisClusterService.hmget(REDIS_SKU_SN_NO_HASH, new ArrayList<Object>(skuSnList));

    int size = skuSnList.size();

    WeakHashMap<String, MaterialResponse> weakHashMap = new WeakHashMap<>(size);
    for (int i = 0; i < size; i++) {
      MaterialResponse response = new MaterialResponse();
      String skuSn = skuSnList.get(i);
      response.setSkuSn(skuSn);
      response.setSkuNo((String) skuNoList.get(i));

      weakHashMap.put(skuSn, response);
    }

    return weakHashMap;
  }

  /**
   * 通过skuSn列表获取物料列表HashMap,key为skuSn, value为MaterialResponse
   */
  public WeakHashMap<String, MaterialResponse> getSkusBySkuSns(List<String> skuSnList) {

    String skuSns = String.join(",", skuSnList);
    ResponseData responseData = materialService.getSkusBySkuSns(skuSns);

    if (!responseData.getCode().equals(ResponseBackCode.SUCCESS.getValue())) {
      throw new RequestException(
          ResponseBackCode.FILE_NOT_FOUND.getValue(),
          "获取商品服务返回失败:" + responseData.getMsg()
      );
    }

    // 有可能从商品微服务上获取的SKU数量不等于原始给定的SKU数量，
    // 这时候默认放行，要排除缺省的SKU 把下面debug的注释去掉
    List<MaterialResponse> materialResponses = JsonTransformer
        .getObjectMapper()
        .convertValue(responseData.getData(), new TypeReference<List<MaterialResponse>>() {});

//    // debug时开启
    
    // 排除添加相同SKU条目
//    HashSet<String> skuSnSet = new HashSet<String>(skuSnList);
//    if (materialResponses.size() != skuSnSet.size()) {
//
//      StringBuilder msgBuilder = new StringBuilder();
//
//      msgBuilder.append("无法从商品服务获取完整的商品数据--商品微服务缺省的SKUSN：( ");
//
//      HashSet<String> responseNoHashSet = new HashSet<>(10);
//      for(MaterialResponse response : materialResponses) {
//        responseNoHashSet.add(response.getSkuSn());
//      }
//
//      skuSnList.forEach(it -> {
//        if(!responseNoHashSet.contains(it)) {
//          msgBuilder.append(it).append(",");
//        }
//      });
//
//      msgBuilder.append(")");
//     logger.debug(msgBuilder.toString());
//    }
//    // debug时开启


    Map<String, MaterialResponse> skuSnMap = materialResponses.stream().collect(Collectors.toMap(MaterialResponse::getSkuSn, Function.identity()));

    return new WeakHashMap<String, MaterialResponse>(skuSnMap);
  }

  /**
   * 通过skuNo列表 feign服务调用 获取物料列表
   *
   * @return List<MaterialResponse>
   */
  private List<MaterialResponse> getSkusBySkuNos(List<String> skuNoList) {
    String skuNos = String.join(",", skuNoList);

    // 排除添加相同SKU条目
    HashSet<String> skuNoSet = new HashSet<String>(skuNoList);

    ResponseData responseData = materialService.getSkusBySkuNos(skuNos);

    if (!responseData.getCode().equals(ResponseBackCode.SUCCESS.getValue())) {
      throw new RequestException(
          ResponseBackCode.FILE_NOT_FOUND.getValue(),
          "获取商品服务返回失败:" + responseData.getMsg()
      );
    }

    List<MaterialResponse> materialResponses = JsonTransformer
        .getObjectMapper()
        .convertValue(responseData.getData(), new TypeReference<List<MaterialResponse>>() {});

    if (materialResponses.size() != skuNoSet.size()) {

      StringBuilder msgBuilder = new StringBuilder();

      msgBuilder.append("无法从商品服务获取完整的商品数据--请求前skuNos数据( ");
      msgBuilder.append(skuNos);
      msgBuilder.append(" );请求后返回SkuNos数据( ");

      for(MaterialResponse response : materialResponses) {
        msgBuilder.append(response.getSkuNo()).append(",");
      }
      msgBuilder.append(")");

      throw new RequestException(
          ResponseBackCode.FILE_NOT_FOUND.getValue(),
          msgBuilder.toString()
      );
    }

    return materialResponses;
  }

  public List<OrderDetail> getSkus(List<OrderSkuDetailVo> orderSkuDetailVos) {
    int requestDetailSize = orderSkuDetailVos.size();
    List<String> noList = new ArrayList<>(requestDetailSize);
    orderSkuDetailVos.forEach(it -> noList.add(it.getSkuNo()));

    // 通过feign调用经销商-商品微服务获取物料信息
    List<MaterialResponse> materialResponses = getSkusBySkuNos(noList);

    // 获取键值对(skuNo MaterialResponse) 的HashMap
    HashMap<String, MaterialResponse> materialResponseHashMap = new HashMap<>(materialResponses.size());
    materialResponses.forEach(it -> materialResponseHashMap.put(it.getSkuNo(), it));

    List<OrderDetail> details = new ArrayList<>(requestDetailSize);
    orderSkuDetailVos.forEach(it -> {
      OrderDetail detail = new OrderDetail();
      MaterialResponse materialResponse = materialResponseHashMap.get(it.getSkuNo());
      if (Objects.isNull(materialResponse)) {
        throw new RequestException(ResponseBackCode.ERROR_FAIL.getValue(), "获取商品信息为空");
      }
      BeanUtils.copyProperties(materialResponse, detail);
      detail.setQuantity(it.getQuantity());
      detail.setGift(it.getIsGift());
      detail.setPickup(it.getIsPickup());
      detail.setSample(it.getIsSample());
      detail.setAmount(new BigDecimal(it.getAmount()));
      // 原价
      detail.setOriginalAmount(materialResponse.getAmount());
      details.add(detail);
    });

    return details;
  }
}
