package cn.sipin.cloud.order.service.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;


import org.springframework.stereotype.Service;

import java.util.List;

import cn.sipin.cloud.order.service.mapper.OrderDetailMapper;
import cn.sipin.cloud.order.service.service.OrderDetailServiceContract;
import cn.sipin.sales.cloud.order.pojo.OrderDetail;

/**
 * <p>
 * 订单明细 服务实现类
 * </p>
 *
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailServiceContract {

  @Override public List<OrderDetail> selectListByOrderIds(List<Long> orderIds) {
    return baseMapper.selectListByOrderIds(orderIds);
  }

  @Override public List<OrderDetail> selectListByOrderDetailIds(List<Long> orderDetailIds) {
    return baseMapper.selectListByOrderDetailIds(orderDetailIds);
  }
}
