package cn.sipin.cloud.order.service.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import cn.sipin.sales.cloud.order.pojo.OrderDiscount;

/**
 * <p>
 * 订单优惠折扣 Mapper 接口
 * </p>
 *
 */
public interface OrderDiscountMapper extends BaseMapper<OrderDiscount> {

  OrderDiscount selectOrderCouponByOrderId(@Param("orderId") Long orderId);

  List<OrderDiscount> selectOrderDiscountByOrderId(@Param("orderId") Long orderId);
}
