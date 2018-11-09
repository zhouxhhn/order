package cn.sipin.cloud.order.service.service;

import com.baomidou.mybatisplus.service.IService;

import cn.sipin.sales.cloud.order.pojo.OrderExpress;
import cn.sipin.sales.cloud.order.pojo.Orders;
import cn.sipin.sales.cloud.order.request.erp.ErpOrderExpressRequest;

/**
 * <p>
 * 订单快递物流表 服务类
 * </p>
 *
 */
public interface OrderExpressServiceContract extends IService<OrderExpress> {

  Boolean create(Orders orders, ErpOrderExpressRequest request);
}
