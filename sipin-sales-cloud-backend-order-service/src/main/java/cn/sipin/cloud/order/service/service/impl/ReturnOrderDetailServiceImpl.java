package cn.sipin.cloud.order.service.service.impl;

import com.google.common.collect.Lists;

import cn.sipin.cloud.order.service.service.OrderDetailServiceContract;
import cn.sipin.sales.cloud.order.dto.returnorderdetail.ReturnOrderDetailDto;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import cn.sipin.cloud.order.service.mapper.ReturnOrderDetailMapper;
import cn.sipin.cloud.order.service.service.ReturnOrderDetailServiceContract;
import cn.sipin.sales.cloud.order.pojo.OrderDetail;
import cn.sipin.sales.cloud.order.pojo.ReturnOrderDetail;
import cn.sipin.sales.cloud.order.response.vo.ReturnOrderDetailVo;

/**
 * <p>
 * 销售退货单明细 服务实现类
 * </p>
 *
 */
@Service
public class ReturnOrderDetailServiceImpl extends ServiceImpl<ReturnOrderDetailMapper, ReturnOrderDetail> implements ReturnOrderDetailServiceContract {

  @Autowired
  private ReturnOrderDetailMapper returnOrderDetailMapper;

  @Autowired
  private OrderDetailServiceContract orderDetailService;



  @Override public List<ReturnOrderDetailVo> selectReturnOrderDetail(Long returnOrderId) {
    List<ReturnOrderDetail> souceDetails = this.selectList(new EntityWrapper<>(new ReturnOrderDetail(returnOrderId)));

    List<Long> orderDetailIds = souceDetails.stream().map(it -> it.getOrderDetailId()).collect(Collectors.toList());
    List<OrderDetail> orderDetails = orderDetailService.selectListByOrderDetailIds(orderDetailIds);
    HashMap<Long, OrderDetail> orderDetailHashMap = new HashMap<>(orderDetails.size());
    for(OrderDetail orderDetail : orderDetails) {
      orderDetailHashMap.put(orderDetail.getId(), orderDetail);
    }

    List<ReturnOrderDetailVo> targetReturnOrderDetailVos = new ArrayList<>(souceDetails.size());

    souceDetails.forEach(it -> {
      ReturnOrderDetailVo targetDetailVo = new ReturnOrderDetailVo();
      BeanUtils.copyProperties(it, targetDetailVo);
      OrderDetail orderDetail = orderDetailHashMap.get(it.getOrderDetailId());
      targetDetailVo.setName(orderDetail.getName());
      targetDetailVo.setSalesPrice(orderDetail.getRealSingleAmount());
      targetDetailVo.setColor(orderDetail.getColor());
      targetDetailVo.setSpecification(orderDetail.getSpecification());
      targetDetailVo.setTexture(orderDetail.getTexture());
      targetDetailVo.setImgPath(orderDetail.getImgPath());
      targetReturnOrderDetailVos.add(targetDetailVo);
    });

    souceDetails = null;

    return targetReturnOrderDetailVos;
  }

  @Override
  public List<ReturnOrderDetailDto> getListByReturnOrderIds(List<Long> idList) {
    return returnOrderDetailMapper.getListByReturnOrderIds(idList);
  }
}
