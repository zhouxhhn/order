package cn.sipin.cloud.order.service.service;

import com.baomidou.mybatisplus.service.IService;

import cn.sipin.sales.cloud.order.pojo.OrderDiscount;

/**
 * <p>
 * 订单优惠折扣 服务类
 * </p>
 *
 */
public interface OrderDiscountServiceContract extends IService<OrderDiscount> {

  OrderDiscount selectOrderCouponByOrderId(Long orderId);
}
