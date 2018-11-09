package cn.sipin.cloud.order.service.service;

import cn.sipin.sales.cloud.order.dto.returnorderdetail.ReturnOrderDetailDto;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

import cn.sipin.sales.cloud.order.pojo.ReturnOrderDetail;
import cn.sipin.sales.cloud.order.response.vo.ReturnOrderDetailVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 销售退货单明细 服务类
 * </p>
 *
 */
public interface ReturnOrderDetailServiceContract extends IService<ReturnOrderDetail> {

  List<ReturnOrderDetailVo> selectReturnOrderDetail(Long id);

  public List<ReturnOrderDetailDto> getListByReturnOrderIds(List<Long> idList);
}
