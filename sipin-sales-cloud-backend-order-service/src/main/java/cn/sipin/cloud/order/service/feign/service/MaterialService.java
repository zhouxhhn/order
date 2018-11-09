package cn.sipin.cloud.order.service.feign.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.sipin.cloud.order.service.feign.callback.MaterialServiceFallBack;
import cn.siyue.platform.base.ResponseData;

/**
 * <p>
 * 服务类
 * </p>
 */
@FeignClient(name = "merchandise-service", fallback = MaterialServiceFallBack.class)
public interface MaterialService {

  @RequestMapping(value = "/sku/with-stock", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData index(
      @RequestParam(value = "page") Integer page,
      @RequestParam(value = "size") Integer size,
      @RequestParam(value = "search") String search
  );


  @RequestMapping(value = "/sku/without-stock", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData indexWithoutStock(
      @RequestParam(value = "page") Integer page,
      @RequestParam(value = "size") Integer size,
      @RequestParam(value = "search") String search
  );


  /**
   * 不对前端展示调用，只用于服务间调用
   * @param skuNos
   * @return
   */
  @RequestMapping(value = "/sku/sku-list", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData getSkusBySkuNos(
      @RequestParam(value = "skuNos") String skuNos
  );

  /**
   * 不对前端展示调用，只用于服务间调用
   * @param skuNos
   * @return
   */
  @RequestMapping(value = "/sku/sku-list", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData getSkusBySkuNos(
      @RequestParam(value = "skuNos") String skuNos,
      @RequestHeader(value = "token") String token
  );

  /**
   * 不对前端展示调用，只用于服务间调用
   * @param skuSns
   * @return
   */
  @RequestMapping(value = "/sku/sku-list-by-sn", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData getSkusBySkuSns(
      @RequestParam(value = "skuSns") String skuSns
  );

  @RequestMapping(value = "/sku/get-sku-by-group-number", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
  ResponseData getskuBygroupNumber( @RequestParam(value = "skuNumber") String skuNumber
  );
}
