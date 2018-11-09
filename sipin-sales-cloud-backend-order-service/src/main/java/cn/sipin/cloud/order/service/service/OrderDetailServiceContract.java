package cn.sipin.cloud.order.service.service;

import com.baomidou.mybatisplus.service.IService;

import java.util.List;

import cn.sipin.sales.cloud.order.pojo.OrderDetail;

/**
 * <p>
 * 订单明细 服务类
 * </p>
 *
 */
public interface OrderDetailServiceContract extends IService<OrderDetail> {

  List<OrderDetail> selectListByOrderIds(List<Long> orderIds);

  List<OrderDetail> selectListByOrderDetailIds(List<Long> orderDetailIds);
}
