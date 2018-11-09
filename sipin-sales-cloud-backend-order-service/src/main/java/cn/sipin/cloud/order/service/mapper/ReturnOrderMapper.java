package cn.sipin.cloud.order.service.mapper;

import cn.sipin.cloud.order.service.mapper.param.ReturnOrderSearchParam;
import cn.sipin.sales.cloud.order.dto.returnorder.ReturnOrderDto;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.Param;

import java.util.List;

import cn.sipin.sales.cloud.order.pojo.ReturnOrder;
import cn.sipin.sales.cloud.order.request.returnOrder.IndexReturnOrdersRequest;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 销售退货单 Mapper 接口
 * </p>
 *
 */
@Repository
public interface ReturnOrderMapper extends BaseMapper<ReturnOrder> {

  ReturnOrder selectByNo(@Param("no") String no);

  List<ReturnOrder> selectReturnOrderPage(@Param("order") ReturnOrder returnOrder, @Param("limit") Integer limit,  @Param("offset") Integer offset,  @Param("request")
      IndexReturnOrdersRequest request);

  Integer selectReturnOrderPageCount(@Param("order")ReturnOrder returnOrder, @Param("request") IndexReturnOrdersRequest request);

  List<ReturnOrderDto> search(@Param("queryParam") ReturnOrderSearchParam queryParam, Pagination page);
}
