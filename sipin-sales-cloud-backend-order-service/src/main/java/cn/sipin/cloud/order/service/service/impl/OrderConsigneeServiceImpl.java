package cn.sipin.cloud.order.service.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;


import org.springframework.stereotype.Service;

import cn.sipin.cloud.order.service.mapper.OrderConsigneeMapper;
import cn.sipin.cloud.order.service.service.OrderConsigneeServiceContract;
import cn.sipin.sales.cloud.order.pojo.OrderConsignee;

/**
 * <p>
 * 订单收货人 服务实现类
 * </p>
 *
 */
@Service
public class OrderConsigneeServiceImpl extends ServiceImpl<OrderConsigneeMapper, OrderConsignee> implements OrderConsigneeServiceContract {

}
