package cn.sipin.cloud.order.service.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import cn.sipin.cloud.order.service.mapper.OrderExpressMapper;
import cn.sipin.cloud.order.service.service.OrderExpressServiceContract;
import cn.sipin.sales.cloud.order.constants.OrderStatus;
import cn.sipin.sales.cloud.order.pojo.OrderExpress;
import cn.sipin.sales.cloud.order.pojo.Orders;
import cn.sipin.sales.cloud.order.request.erp.ErpOrderExpressRequest;

/**
 * <p>
 * 订单快递物流表 服务实现类
 * </p>
 */
@Service
public class OrderExpressServiceImpl extends ServiceImpl<OrderExpressMapper, OrderExpress> implements OrderExpressServiceContract {

  @Transactional(rollbackFor = Exception.class)
  @Override public Boolean create(Orders orders, ErpOrderExpressRequest request) {
    if (orders.getStatusId().compareTo(OrderStatus.WAIT_DELIVERY.getCode()) >= 0 &&
        orders.getStatusId().compareTo(OrderStatus.PARTIAL_DELIVERED.getCode()) <= 0) {

      OrderExpress express = new OrderExpress();
      express.setOrderNo(request.getOrderNo());
      express.setExpressNo(request.getExpressNo());
      OrderExpress tempExpress = this.baseMapper.selectOne(express);
      if (Objects.isNull(tempExpress) || Objects.isNull(tempExpress.getId())) {
        express.setExpressCompany(request.getExpressCompanyName());
        express.setExpressCompanyCode(request.getCompanyCode());
        this.insert(express);

        return true;
      }

      // 证明已有了记录 返回已创建
      return true;
    }

    return false;
  }
}
