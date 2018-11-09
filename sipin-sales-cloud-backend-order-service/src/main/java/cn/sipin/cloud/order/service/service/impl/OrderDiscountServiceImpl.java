package cn.sipin.cloud.order.service.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;

import cn.sipin.cloud.order.service.mapper.OrderDiscountMapper;
import cn.sipin.cloud.order.service.service.OrderDiscountServiceContract;
import cn.sipin.sales.cloud.order.pojo.OrderDiscount;

/**
 * <p>
 * 订单优惠折扣 服务实现类
 * </p>
 *
 * @author ${author}
 * @since 2018-08-02
 */
@Service
public class OrderDiscountServiceImpl extends ServiceImpl<OrderDiscountMapper, OrderDiscount> implements OrderDiscountServiceContract {

  @Override public OrderDiscount selectOrderCouponByOrderId(Long orderId) {
    return this.baseMapper.selectOrderCouponByOrderId(orderId);
  }
}
