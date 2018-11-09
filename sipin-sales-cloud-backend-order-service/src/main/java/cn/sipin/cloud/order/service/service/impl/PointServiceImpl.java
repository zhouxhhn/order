/*
 * (C) Copyright 2018 Siyue Holding Group.
 */
package cn.sipin.cloud.order.service.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import cn.sipin.cloud.order.service.contract.Loggable;
import cn.sipin.cloud.order.service.service.OrdersServiceContract;
import cn.sipin.cloud.order.service.service.PointServiceContract;
import cn.sipin.cloud.order.service.service.ReturnOrderServiceContract;
import cn.sipin.cloud.order.service.sipin.constant.PointType;
import cn.sipin.sales.cloud.order.constants.AuditStatus;
import cn.sipin.sales.cloud.order.constants.OrderStatus;
import cn.sipin.sales.cloud.order.pojo.Orders;
import cn.sipin.sales.cloud.order.pojo.ReturnOrder;

@Service
public class PointServiceImpl implements PointServiceContract, Loggable {

  private OrdersServiceContract ordersService;

  private ReturnOrderServiceContract returnOrderService;

  private SipinServiceImpl sipinService;

  @Autowired
  public PointServiceImpl(OrdersServiceContract ordersService, ReturnOrderServiceContract returnOrderService, SipinServiceImpl sipinService) {
    this.ordersService = ordersService;
    this.returnOrderService = returnOrderService;
    this.sipinService = sipinService;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean resumePointByReturnOrder(Long orderId) {

    Orders orders = ordersService.selectById(orderId);
    if (!orders.getStatusId().equals(OrderStatus.FINISHED.getCode())) {
      return false;
    }

    if (orders.getMobile().equals(OrdersServiceImpl.ANONYMOUS_USER)) {
      return false;
    }

    if (orders.getGivePoint().equals(orders.getReturnPoint())) {
      // 赠送积分等于已退积分，说明已全部退完积分
      return true;
    }

    List<ReturnOrder> filterReturnOrders = returnOrderService.selectList(new EntityWrapper<>(new ReturnOrder(orderId)));
    filterReturnOrders = filterReturnOrders.stream().filter(it -> it.getIsReturnPoint().equals(false) && it.getStatusId().equals(AuditStatus.AUDITED.getValue()))
        .collect(Collectors.toList());

    if (filterReturnOrders.size() == 0) {
      return true;
    }

    BigDecimal refundedAmount = BigDecimal.ZERO;
    for (ReturnOrder returnOrder : filterReturnOrders) {
      refundedAmount = refundedAmount.add(returnOrder.getRefundedAmount());
    }

    if (refundedAmount.compareTo(BigDecimal.ZERO) > 0) {

      int minusPoint = refundedAmount.intValue() * (-1);
      //核销积分
      sipinService.updatePoint(orders.getMobile(), minusPoint, PointType.DECREASE.getValue(), orders.getNo());
      logger().info("订单({})已退积分：{}", orders.getNo(), Math.abs(minusPoint));

      // 更新退货退款单已退积分标识
      filterReturnOrders.forEach(it -> {
        ReturnOrder tempOrder = new ReturnOrder();
        tempOrder.setId(it.getId());
        tempOrder.setIsReturnPoint(true);
        tempOrder.setReturnPoint(it.getRefundedAmount().intValue());
        returnOrderService.updateById(tempOrder);
      });
    }

    // 更新订单已退积分
    List<ReturnOrder> returnOrders = returnOrderService.selectList(new EntityWrapper<>(new ReturnOrder(orderId)));
    returnOrders = returnOrders.stream().filter(it -> it.getIsReturnPoint().equals(true) && it.getStatusId().equals(AuditStatus.AUDITED.getValue()))
        .collect(Collectors.toList());
    int returnPoint = 0;
    for (ReturnOrder returnOrder : returnOrders) {
      returnPoint += returnOrder.getReturnPoint();
    }
    Orders tempOrders = new Orders();
    tempOrders.setId(orders.getId());
    tempOrders.setReturnPoint(returnPoint);
    ordersService.updateById(tempOrders);

    return true;
  }
}
