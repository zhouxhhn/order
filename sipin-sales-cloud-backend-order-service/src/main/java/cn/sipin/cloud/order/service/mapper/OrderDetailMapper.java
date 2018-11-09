package cn.sipin.cloud.order.service.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import cn.sipin.sales.cloud.order.pojo.OrderDetail;

/**
 * <p>
 * 订单明细 Mapper 接口
 * </p>
 *
 */
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

  List<OrderDetail> selectListByOrderIds(@Param("orderIds") List<Long> orderIds);

  List<OrderDetail> selectListByOrderDetailIds(@Param("orderDetailIds") List<Long> orderDetailIds);
}
